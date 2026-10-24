import java.sql.*;
import java.util.Scanner;

public class StudentDAO {
    public void addStudent(String roll, String name, String dept, int semester) throws SQLException {
        String sql = "INSERT INTO students(roll_no, student_name, department, current_semester) VALUES(?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, roll);
            p.setString(2, name);
            p.setString(3, dept.toUpperCase());
            p.setInt(4, semester);
            p.executeUpdate();
        }
    }

    public void listStudents() throws SQLException {
        String sql = "SELECT roll_no, student_name, department, current_semester FROM students ORDER BY roll_no";
        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery(sql)) {
            System.out.println("\nROLL NO       NAME                         DEPARTMENT  SEM");
            System.out.println("------------------------------------------------------------");
            while (r.next()) {
                System.out.printf("%-13s %-28s %-11s %d%n",
                    r.getString("roll_no"), r.getString("student_name"),
                    r.getString("department"), r.getInt("current_semester"));
            }
        }
    }

    public void addCourse(String code, String title, double credits, String type) throws SQLException {
        String sql = "INSERT INTO courses(course_code, title, credits, course_type) VALUES(?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, code);
            p.setString(2, title);
            p.setDouble(3, credits);
            p.setString(4, type.toUpperCase());
            p.executeUpdate();
        }
    }

    public void listCourses() throws SQLException {
        String sql = "SELECT course_code, title, credits, course_type FROM courses ORDER BY course_code";
        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery(sql)) {
            System.out.println("\nCOURSE CODE   TITLE                                      CREDITS TYPE");
            System.out.println("-----------------------------------------------------------------------");
            while (r.next()) {
                System.out.printf("%-13s %-42s %-7.1f %s%n",
                    r.getString("course_code"), r.getString("title"),
                    r.getDouble("credits"), r.getString("course_type"));
            }
        }
    }
}
