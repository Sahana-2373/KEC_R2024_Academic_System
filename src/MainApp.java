import java.sql.SQLException;
import java.util.Scanner;

public class MainApp {
    private static final Scanner in = new Scanner(System.in);
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final AcademicDAO academicDAO = new AcademicDAO();

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println(" KEC R2024 STUDENT ACADEMIC & CONTINUOUS EVALUATION SYSTEM");
        System.out.println("==========================================================");
        try {
            DBConnection.getConnection().close();
            System.out.println("Database connection successful.");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            System.out.println("Check MySQL service, schema, username/password and Connector/J.");
            return;
        }

        while (true) {
            System.out.println("\nMAIN MENU");
            System.out.println("1. Add student");
            System.out.println("2. List students");
            System.out.println("3. Add course");
            System.out.println("4. List courses");
            System.out.println("5. Enroll student in course");
            System.out.println("6. Update attendance / medical condonation");
            System.out.println("7. Phase 1: Enter CIA Test 1 and CIA Test 2");
            System.out.println("8. Phase 2: Enter assignment / quiz / lab marks");
            System.out.println("9. Phase 3: Enter ESE marks or mark AB");
            System.out.println("10. Generate student semester mark sheet");
            System.out.println("11. Attendance defaulter report (<65%)");
            System.out.println("12. Department-wise class performance summary");
            System.out.println("0. Exit");
            int choice = readInt("Choose: ");
            try {
                switch (choice) {
                    case 1: addStudent(); break;
                    case 2: studentDAO.listStudents(); break;
                    case 3: addCourse(); break;
                    case 4: studentDAO.listCourses(); break;
                    case 5: enroll(); break;
                    case 6: attendance(); break;
                    case 7: phase1(); break;
                    case 8: phase2(); break;
                    case 9: phase3(); break;
                    case 10: academicDAO.printMarkSheet(readText("Roll number: ").toUpperCase(),
                                                          readInt("Semester: ")); break;
                    case 11: academicDAO.attendanceDefaulters(); break;
                    case 12: academicDAO.classSummary(); break;
                    case 0: System.out.println("Goodbye."); return;
                    default: System.out.println("Choose a listed option.");
                }
            } catch (SQLException | IllegalArgumentException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private static void addStudent() throws SQLException {
        String roll = readText("Roll number (e.g. 24ITR001): ").toUpperCase();
        String name = readText("Student name: ");
        String dept = readText("Department (CSE/ECE/IT/MECH/etc.): ").toUpperCase();
        int sem = readInt("Current semester (1-8): ");
        if (sem < 1 || sem > 8) throw new IllegalArgumentException("Semester must be 1-8.");
        studentDAO.addStudent(roll, name, dept, sem);
        System.out.println("Student added successfully.");
    }

    private static void addCourse() throws SQLException {
        String code = readText("Course code (e.g. 24ITR31): ").toUpperCase();
        String title = readText("Course title: ");
        double credits = readDouble("Credits (e.g. 3): ");
        String type = readText("Course type (THEORY/PRACTICAL/EMBEDDED): ").toUpperCase();
        if (!type.equals("THEORY") && !type.equals("PRACTICAL") && !type.equals("EMBEDDED"))
            throw new IllegalArgumentException("Invalid course type.");
        studentDAO.addCourse(code, title, credits, type);
        System.out.println("Course added successfully.");
    }

    private static void enroll() throws SQLException {
        String roll = readText("Roll number: ").toUpperCase();
        String code = readText("Course code: ").toUpperCase();
        int sem = readInt("Semester: ");
        academicDAO.enroll(roll, code, sem);
        System.out.println("Enrollment created.");
    }

    private static void attendance() throws SQLException {
        String roll = readText("Roll number: ").toUpperCase();
        String code = readText("Course code: ").toUpperCase();
        int sem = readInt("Semester: ");
        int held = readInt("Total classes held: ");
        int attended = readInt("Classes attended: ");
        boolean approved = readYesNo("Medical condonation approved? (y/n): ");
        academicDAO.updateAttendance(roll, code, sem, held, attended, approved);
        double pct = held == 0 ? 0 : 100.0 * attended / held;
        System.out.printf("Attendance saved: %.2f%%%n", pct);
        if (pct < 65) System.out.println("Rule: SA / barred from ESE.");
        else if (pct < 75 && !approved) System.out.println("Rule: ESE eligibility pending medical condonation approval.");
        else System.out.println("Rule: Eligible for ESE.");
    }

    private static void phase1() throws SQLException {
        String roll = readText("Roll number: ").toUpperCase();
        String code = readText("Course code: ").toUpperCase(); int sem = readInt("Semester: ");
        Double c1 = readMark("CIA Test 1 (0-100): ");
        Double c2 = readMark("CIA Test 2 (0-100): ");
        academicDAO.enterMarks(roll, code, sem, "PHASE1", c1, c2, null, null, null, null, false);
        System.out.println("Phase 1 marks saved.");
    }

    private static void phase2() throws SQLException {
        String roll = readText("Roll number: ").toUpperCase();
        String code = readText("Course code: ").toUpperCase(); int sem = readInt("Semester: ");
        Double a = readOptionalMark("Assignment mark (0-100, blank to skip): ");
        Double q = readOptionalMark("Quiz mark (0-100, blank to skip): ");
        Double lab = readOptionalMark("Lab evaluation mark (0-100, blank to skip): ");
        academicDAO.enterMarks(roll, code, sem, "PHASE2", null, null, a, q, lab, null, false);
        System.out.println("Phase 2 marks saved.");
    }

    private static void phase3() throws SQLException {
        String roll = readText("Roll number: ").toUpperCase();
        String code = readText("Course code: ").toUpperCase(); int sem = readInt("Semester: ");
        boolean absent = readYesNo("Was the student absent in ESE? (y/n): ");
        Double ese = absent ? null : readMark("ESE mark (0-100): ");
        academicDAO.enterMarks(roll, code, sem, "PHASE3", null, null, null, null, null, ese, absent);
        System.out.println(absent ? "ESE absence recorded as AB." : "ESE marks saved.");
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        String s = in.nextLine().trim();
        if (s.isEmpty()) throw new IllegalArgumentException("Input cannot be blank.");
        return s;
    }
    private static int readInt(String prompt) {
        try { return Integer.parseInt(readText(prompt)); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Enter a whole number."); }
    }
    private static double readDouble(String prompt) {
        try { return Double.parseDouble(readText(prompt)); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Enter a valid number."); }
    }
    private static Double readMark(String prompt) {
        double v = readDouble(prompt);
        if (v < 0 || v > 100) throw new IllegalArgumentException("Marks must be 0-100.");
        return v;
    }
    private static Double readOptionalMark(String prompt) {
        System.out.print(prompt);
        String s = in.nextLine().trim();
        if (s.isEmpty()) return null;
        try {
            double v = Double.parseDouble(s);
            if (v < 0 || v > 100) throw new IllegalArgumentException("Marks must be 0-100.");
            return v;
        } catch (NumberFormatException e) { throw new IllegalArgumentException("Enter a valid number or leave blank."); }
    }
    private static boolean readYesNo(String prompt) {
        String s = readText(prompt).toLowerCase();
        if (s.equals("y") || s.equals("yes")) return true;
        if (s.equals("n") || s.equals("no")) return false;
        throw new IllegalArgumentException("Enter y or n.");
    }
}
