import java.sql.*;
import java.util.Locale;

public class AcademicDAO {
    public void enroll(String roll, String code, int semester) throws SQLException {
        String sql = "INSERT INTO enrollments(roll_no, course_code, semester) VALUES(?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, roll);
            p.setString(2, code);
            p.setInt(3, semester);
            p.executeUpdate();
        }
        ensureMarksRow(roll, code, semester);
    }

    private long findEnrollmentId(Connection c, String roll, String code, int semester) throws SQLException {
        String sql = "SELECT enrollment_id FROM enrollments WHERE roll_no=? AND course_code=? AND semester=?";
        try (PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, roll);
            p.setString(2, code);
            p.setInt(3, semester);
            try (ResultSet r = p.executeQuery()) {
                if (!r.next()) throw new SQLException("Enrollment not found. Enroll the student first.");
                return r.getLong(1);
            }
        }
    }

    private void ensureMarksRow(String roll, String code, int semester) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            long id = findEnrollmentId(c, roll, code, semester);
            try (PreparedStatement p = c.prepareStatement(
                    "INSERT IGNORE INTO assessment_marks(enrollment_id) VALUES(?)")) {
                p.setLong(1, id);
                p.executeUpdate();
            }
        }
    }

    public void updateAttendance(String roll, String code, int semester,
                                 int held, int attended, boolean condonation) throws SQLException {
        if (held < 0 || attended < 0 || attended > held)
            throw new IllegalArgumentException("Attendance must satisfy 0 <= attended <= classes held.");
        String sql = "UPDATE enrollments SET classes_held=?, classes_attended=?, medical_condonation_approved=? " +
                     "WHERE roll_no=? AND course_code=? AND semester=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, held);
            p.setInt(2, attended);
            p.setBoolean(3, condonation);
            p.setString(4, roll);
            p.setString(5, code);
            p.setInt(6, semester);
            if (p.executeUpdate() == 0) throw new SQLException("Enrollment not found.");
        }
    }

    public void enterMarks(String roll, String code, int semester, String phase,
                           Double cia1, Double cia2, Double assignment, Double quiz,
                           Double lab, Double ese, boolean absent) throws SQLException {
        ensureMarksRow(roll, code, semester);
        String sql = "UPDATE assessment_marks m JOIN enrollments e ON e.enrollment_id=m.enrollment_id " +
                     "SET m.cia1=COALESCE(?,m.cia1), m.cia2=COALESCE(?,m.cia2), " +
                     "m.assignment_mark=COALESCE(?,m.assignment_mark), m.quiz_mark=COALESCE(?,m.quiz_mark), " +
                     "m.lab_mark=COALESCE(?,m.lab_mark), m.ese_mark=CASE WHEN ? THEN NULL ELSE COALESCE(?,m.ese_mark) END, " +
                     "m.ese_absent=CASE WHEN ? THEN TRUE ELSE m.ese_absent END " +
                     "WHERE e.roll_no=? AND e.course_code=? AND e.semester=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            setNullable(p, 1, cia1); setNullable(p, 2, cia2);
            setNullable(p, 3, assignment); setNullable(p, 4, quiz);
            setNullable(p, 5, lab);
            p.setBoolean(6, absent); setNullable(p, 7, ese);
            p.setBoolean(8, absent);
            p.setString(9, roll); p.setString(10, code); p.setInt(11, semester);
            if (p.executeUpdate() == 0) throw new SQLException("Enrollment not found.");
        }
    }

    private void setNullable(PreparedStatement p, int index, Double value) throws SQLException {
        if (value == null) p.setNull(index, Types.DECIMAL);
        else {
            if (value < 0 || value > 100) throw new IllegalArgumentException("Marks must be from 0 to 100.");
            p.setDouble(index, value);
        }
    }

    private static double attendancePercent(int held, int attended) {
        return held == 0 ? 0.0 : 100.0 * attended / held;
    }

    private static double ciaRaw(String type, Double c1, Double c2, Double assignment, Double quiz, Double lab) {
        // Prototype rule: average the applicable entered components, ignoring components not yet entered.
        double sum = 0; int n = 0;
        Double[] values = "PRACTICAL".equals(type)
            ? new Double[]{c1, c2, lab}
            : "EMBEDDED".equals(type)
                ? new Double[]{c1, c2, assignment, quiz, lab}
                : new Double[]{c1, c2, assignment, quiz};
        for (Double v : values) if (v != null) { sum += v; n++; }
        return n == 0 ? 0 : sum / n;
    }

    private static String gradeFor(double total) {
        if (total >= 91) return "O";
        if (total >= 81) return "A+";
        if (total >= 71) return "A";
        if (total >= 61) return "B+";
        if (total >= 50) return "B";
        return "RA";
    }

    private static int pointsFor(String grade) {
        switch (grade) {
            case "O": return 10;
            case "A+": return 9;
            case "A": return 8;
            case "B+": return 7;
            case "B": return 6;
            default: return 0;
        }
    }

    private static class ResultRow {
        String roll, name, dept, code, title, type, grade, status;
        double credits, attendance, ciaRaw, ciaWeighted, eseRaw, eseWeighted, total;
        int held, attended, points;
    }

    private ResultRow readResult(ResultSet r) throws SQLException {
        ResultRow x = new ResultRow();
        x.roll = r.getString("roll_no"); x.name = r.getString("student_name");
        x.dept = r.getString("department"); x.code = r.getString("course_code");
        x.title = r.getString("title"); x.type = r.getString("course_type");
        x.credits = r.getDouble("credits"); x.held = r.getInt("classes_held");
        x.attended = r.getInt("classes_attended");
        x.attendance = attendancePercent(x.held, x.attended);
        Double c1 = nullableDouble(r, "cia1"), c2 = nullableDouble(r, "cia2");
        Double a = nullableDouble(r, "assignment_mark"), q = nullableDouble(r, "quiz_mark");
        Double lab = nullableDouble(r, "lab_mark"), ese = nullableDouble(r, "ese_mark");
        boolean absent = r.getBoolean("ese_absent");
        x.ciaRaw = ciaRaw(x.type, c1, c2, a, q, lab);
        double ciaWeight = "THEORY".equals(x.type) ? 0.40 : "PRACTICAL".equals(x.type) ? 0.60 : 0.50;
        double eseWeight = 1.0 - ciaWeight;
        x.ciaWeighted = x.ciaRaw * ciaWeight;
        x.eseRaw = ese == null ? 0 : ese;
        x.eseWeighted = x.eseRaw * eseWeight;
        x.total = x.ciaWeighted + x.eseWeighted;

        if (x.attendance < 65.0) {
            x.grade = "SA"; x.status = "BARRED: ATTENDANCE"; x.points = 0;
        } else if (x.attendance < 75.0 && !r.getBoolean("medical_condonation_approved")) {
            x.grade = "SA"; x.status = "CONDONATION PENDING"; x.points = 0;
        } else if (absent) {
            x.grade = "AB"; x.status = "ABSENT IN ESE"; x.points = 0;
        } else if (ese == null) {
            x.grade = "PENDING"; x.status = "ESE NOT ENTERED"; x.points = 0;
        } else {
            double minESE = "THEORY".equals(x.type) ? 45.0 : "PRACTICAL".equals(x.type) ? 50.0 : 45.0;
            if (x.eseRaw < minESE || x.total < 50.0) {
                x.grade = "RA"; x.status = "FAIL"; x.points = 0;
            } else {
                x.grade = gradeFor(x.total); x.status = "PASS"; x.points = pointsFor(x.grade);
            }
        }
        return x;
    }

    private Double nullableDouble(ResultSet r, String col) throws SQLException {
        double value = r.getDouble(col);
        return r.wasNull() ? null : value;
    }

    private String baseReportSql() {
        return "SELECT s.roll_no,s.student_name,s.department,e.course_code,e.semester,e.classes_held,e.classes_attended," +
               "e.medical_condonation_approved,c.title,c.credits,c.course_type,m.cia1,m.cia2,m.assignment_mark," +
               "m.quiz_mark,m.lab_mark,m.ese_mark,m.ese_absent FROM enrollments e " +
               "JOIN students s ON s.roll_no=e.roll_no JOIN courses c ON c.course_code=e.course_code " +
               "LEFT JOIN assessment_marks m ON m.enrollment_id=e.enrollment_id ";
    }

    public void printMarkSheet(String roll, int semester) throws SQLException {
        String sql = baseReportSql() + "WHERE s.roll_no=? AND e.semester=? ORDER BY e.course_code";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, roll); p.setInt(2, semester);
            try (ResultSet r = p.executeQuery()) {
                double weightedPoints = 0, totalCredits = 0; boolean any = false;
                System.out.println("\n================ KEC R2024 - SEMESTER MARK SHEET ================");
                System.out.printf("Roll No: %s | Semester: %d%n", roll, semester);
                System.out.println("------------------------------------------------------------------");
                System.out.printf("%-10s %-10s %-6s %-6s %-7s %-8s %-12s%n",
                    "COURSE", "TYPE", "CIA", "ESE", "TOTAL", "GRADE", "STATUS");
                System.out.println("------------------------------------------------------------------");
                while (r.next()) {
                    any = true; ResultRow x = readResult(r);
                    System.out.printf(Locale.US, "%-10s %-10s %6.1f %6.1f %7.1f %-8s %-12s%n",
                        x.code, x.type, x.ciaWeighted, x.eseWeighted, x.total, x.grade, x.status);
                    if (x.points > 0) { weightedPoints += x.credits * x.points; totalCredits += x.credits; }
                    else if ("RA".equals(x.grade) || "SA".equals(x.grade) || "AB".equals(x.grade)) totalCredits += x.credits;
                    else totalCredits += x.credits;
                }
                if (!any) { System.out.println("No enrollments found for this student/semester."); return; }
                double sgpa = totalCredits == 0 ? 0 : weightedPoints / totalCredits;
                System.out.println("------------------------------------------------------------------");
                System.out.printf(Locale.US, "SGPA: %.2f | Equivalent percentage (SGPA x 10): %.2f%%%n",
                    sgpa, sgpa * 10.0);
                System.out.println("Note: Pending assessments and non-passing grades contribute 0 grade points.");
            }
        }
    }

    public void attendanceDefaulters() throws SQLException {
        String sql = baseReportSql() + "WHERE e.classes_held > 0 AND (100.0*e.classes_attended/e.classes_held) < 65 " +
                     "ORDER BY s.department,s.roll_no";
        try (Connection c = DBConnection.getConnection(); Statement s = c.createStatement();
             ResultSet r = s.executeQuery(sql)) {
            System.out.println("\nATTENDANCE DEFAULTERS (BELOW 65%)");
            System.out.printf("%-13s %-24s %-8s %-10s %-8s%n", "ROLL NO", "NAME", "COURSE", "ATTENDANCE", "GRADE");
            while (r.next()) {
                double pct = attendancePercent(r.getInt("classes_held"), r.getInt("classes_attended"));
                System.out.printf(Locale.US, "%-13s %-24s %-8s %8.2f%% %-8s%n",
                    r.getString("roll_no"), r.getString("student_name"), r.getString("course_code"), pct, "SA");
            }
        }
    }

    public void classSummary() throws SQLException {
        String sql = baseReportSql() + "ORDER BY s.department,s.roll_no,e.semester,e.course_code";
        class DeptStats { int total=0, passed=0, failed=0; }
        java.util.Map<String, DeptStats> stats = new java.util.TreeMap<>();
        java.util.Map<String, double[]> studentData = new java.util.HashMap<>();
        try (Connection c = DBConnection.getConnection(); Statement st = c.createStatement();
             ResultSet r = st.executeQuery(sql)) {
            while (r.next()) {
                ResultRow x = readResult(r);
                DeptStats d = stats.computeIfAbsent(x.dept, k -> new DeptStats());
                d.total++;
                if ("PASS".equals(x.status)) d.passed++;
                else if ("FAIL".equals(x.status)) d.failed++;
                String key = x.dept + "|" + x.roll + "|" + r.getInt("semester");
                double[] agg = studentData.computeIfAbsent(key, k -> new double[2]);
                agg[0] += x.credits * x.points;
                agg[1] += x.credits;
            }
        }
        java.util.Map<String, double[]> deptSgpa = new java.util.HashMap<>();
        for (java.util.Map.Entry<String, double[]> e : studentData.entrySet()) {
            String dept = e.getKey().split("\\|", 2)[0];
            double[] v = e.getValue();
            if (v[1] > 0) {
                double[] agg = deptSgpa.computeIfAbsent(dept, k -> new double[2]);
                agg[0] += v[0] / v[1];
                agg[1] += 1;
            }
        }
        System.out.println("\\nDEPARTMENT-WISE RESULT SUMMARY");
        System.out.printf("%-12s %-12s %-10s %-10s %-12s%n", "DEPARTMENT", "ENROLLMENTS", "PASS", "FAIL", "AVG SGPA");
        for (java.util.Map.Entry<String, DeptStats> e : stats.entrySet()) {
            double[] a = deptSgpa.getOrDefault(e.getKey(), new double[2]);
            System.out.printf(Locale.US, "%-12s %-12d %-10d %-10d %.2f%n",
                e.getKey(), e.getValue().total, e.getValue().passed, e.getValue().failed,
                a[1] == 0 ? 0 : a[0] / a[1]);
        }
    }
}
