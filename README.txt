# KEC R2024 Student Academic & Continuous Evaluation System

**Java JDBC + MySQL \| Menu-driven console application**

------------------------------------------------------------------------

## Student Profile

  -----------------------------------------------------------------------
  Detail                              Information
  ----------------------------------- -----------------------------------
  **Name**                            Sahana S

  **College**                         Kongu Engineering College

  **Programme**                       B.Tech -- Information Technology

  **Academic Level**                  Second-Year Student

  **Project Title**                   Development of Kongu Engineering
                                      College (R2024) Student Academic &
                                      Continuous Evaluation System

  **Technologies**                    Java, JDBC, MySQL
  -----------------------------------------------------------------------

### Technical Skills

-   C Programming
-   C++
-   Java
-   HTML
-   Problem Solving

### Coding Profiles

-   **GitHub:** [Sahana-2373](https://github.com/Sahana-2373)
-   **LeetCode:** [sahana_S2373](https://leetcode.com/sahana_S2373/)

------------------------------------------------------------------------

## 1. Project Overview

The **KEC R2024 Student Academic & Continuous Evaluation System** is a
menu-driven Java console application that uses JDBC to communicate with
a MySQL database. It is a student project prototype for managing student
details, course information, enrollment, attendance, assessment marks,
and semester performance reports.

## 2. Objectives

-   Maintain student registration details.
-   Store course information, including course code, title, credits, and
    course type.
-   Manage course enrollment and attendance records.
-   Enter and update assessment marks incrementally.
-   Evaluate academic performance using the rules configured in the
    application.
-   Generate semester mark sheets, attendance-defaulter lists, and
    department result summaries.
-   Calculate grade points and SGPA for enrolled courses.

## 3. Main Features

-   **Student Management:** Register and maintain student records.
-   **Course Catalog:** Store theory, practical, and embedded courses.
-   **Enrollment:** Associate students with courses and semesters.
-   **Attendance Tracking:** Record classes held and attended and
    calculate attendance percentage.
-   **Assessment Entry:** Maintain CIA and End Semester Examination
    (ESE) marks.
-   **Evaluation:** Determine result status and grade point using the
    implemented rules.
-   **Reports:** Display academic results, attendance information, and
    semester summaries.

## 4. Technology Stack

  -----------------------------------------------------------------------
  Technology                          Purpose
  ----------------------------------- -----------------------------------
  Java                                Application logic and menu-driven
                                      interface

  JDBC                                Connection between Java and MySQL

  MySQL Server                        Persistent database

  MySQL Command Line Client           Running SQL commands and checking
                                      records

  Visual Studio Code                  Editing and running Java source
                                      code

  JDK                                 Compiling and executing the
                                      application
  -----------------------------------------------------------------------

## 5. Project Structure

``` text
KEC_R2024_Academic_System/
├── src/
│   ├── DBConnection.java
│   ├── StudentDAO.java
│   ├── AcademicDAO.java
│   └── MainApp.java
├── kec_schema.sql
├── kec_sample_data.sql
├── README.md
└── TEST_CASES.txt
```

-   `DBConnection.java` --- establishes the JDBC connection to MySQL.
-   `StudentDAO.java` --- handles student-related database operations.
-   `AcademicDAO.java` --- handles course, enrollment, attendance,
    assessment, and reporting operations.
-   `MainApp.java` --- displays the menu and coordinates user actions.
-   `kec_schema.sql` --- creates the database tables and structure.
-   `kec_sample_data.sql` --- inserts sample records for testing.
-   `TEST_CASES.txt` --- provides example test scenarios.

## 6. Prerequisites

Install the following on Windows:

1.  **Java Development Kit (JDK)**.
2.  **MySQL Server**.
3.  **MySQL Connector/J** JDBC driver.
4.  **Visual Studio Code** (recommended editor).

Check Java from the VS Code terminal:

``` powershell
java -version
javac -version
```

Both commands should display a version number.

## 7. Database Setup

1.  Start the MySQL Server service.
2.  Open MySQL Command Line Client and sign in.
3.  Create and select the project database:

``` sql
CREATE DATABASE kec_academic_system;
USE kec_academic_system;
```

4.  Run `kec_schema.sql` to create the tables.
5.  Run `kec_sample_data.sql` to insert sample records.

Run the SQL files from the extracted project folder. If the files
already contain `CREATE DATABASE` or `USE` statements, follow those
statements and avoid creating conflicting duplicate databases.

## 8. Configure JDBC

Open `src/DBConnection.java` and set the connection URL, username, and
password to match your local MySQL installation. A typical local URL is:

``` text
jdbc:mysql://localhost:3306/kec_academic_system
```

Use your own MySQL credentials. **Never commit your real database
password to a public repository.** For a public version, use environment
variables or a local configuration file excluded from version control.

Ensure the MySQL Connector/J JAR is available and included in the Java
classpath. Use a driver version compatible with your installed Java
version.

## 9. Run in VS Code

1.  Open the extracted `KEC_R2024_Academic_System` folder in VS Code.
2.  Confirm the JDK is installed and MySQL Server is running.
3.  Configure `DBConnection.java`.
4.  Make sure the Connector/J JAR is available.
5.  Open **Terminal → New Terminal** in VS Code.
6.  Compile the source files, replacing the JAR path and version with
    your actual file:

``` powershell
javac -cp ".;lib\mysql-connector-j-<version>.jar" -d out src\*.java
```

Run the application:

``` powershell
java -cp ".;out;lib\mysql-connector-j-<version>.jar" MainApp
```

Update the classpath if the JAR is stored elsewhere. If the project uses
a package declaration, adjust the run command to match it. When the
program starts successfully, the menu-driven interface appears in the VS
Code terminal.

## 10. Academic Evaluation Rules

The prototype supports **THEORY**, **PRACTICAL**, and **EMBEDDED**
course types and applies the assessment and grade rules implemented in
its Java source code. Verify CIA calculations, ESE minimums,
attendance-condonation rules, grade boundaries, and SGPA calculations
against the current official KEC R2024 regulations before using them for
academic decisions.

Attendance eligibility is based on the thresholds configured in the
application.

> **Academic disclaimer:** This is a student project prototype for
> learning and demonstration. It is not an official KEC academic system,
> and its output must not be treated as an official result or regulation
> document.

## 11. Reports

-   Student semester mark sheet.
-   Attendance-defaulter information.
-   Department-level result summary.
-   Grade points and SGPA based on recorded results and enrolled course
    credits.

Report accuracy depends on the database records and evaluation rules
configured in the source code.

## 12. Testing

Use `TEST_CASES.txt` for example scenarios. Test with sample data before
entering real student information.

Recommended checks: - Register a student and verify the record in
MySQL. - Add or view course details. - Enroll a student in a course. -
Enter attendance and assessment marks. - Generate a semester mark
sheet. - Verify grade, result status, and SGPA. - Check
attendance-defaulter and department-summary reports.

## 13. Future Enhancements

-   Graphical or web-based interface.
-   Secure role-based login for administrators and faculty.
-   Stronger input validation and clearer error messages.
-   PDF or Excel report export.
-   Audit history for marks and attendance changes.
-   Automated tests.
-   External configuration for credentials and institution-specific
    rules.

## 14. Learning Outcomes

This project provides practice in: - Java programming and menu-driven
application design. - JDBC connectivity and database CRUD operations. -
Relational database design using MySQL. - Data Access Object (DAO)
organisation. - Academic calculations, validation, and report
generation. - Testing and documenting a database-backed application.

------------------------------------------------------------------------

## Author

**Sahana S**\
B.Tech Information Technology Student\
Kongu Engineering College

-   GitHub: https://github.com/Sahana-2373
-   LeetCode: https://leetcode.com/sahana_S2373/

*Developed for academic learning and project demonstration.*
