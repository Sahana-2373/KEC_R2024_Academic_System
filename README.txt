<div align="center">

# KEC R2024 Academic Evaluation System

### A Java + JDBC + MySQL Academic Management Application

Manage student records, course enrollment, attendance, assessment marks, semester results, and academic reports through a menu-driven console application.

<br>

![Java](https://img.shields.io/badge/Java-JDBC-F89820?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Interface](https://img.shields.io/badge/Interface-Console-475569?style=for-the-badge)
![Academic Project](https://img.shields.io/badge/Project-KEC%20R2024-166534?style=for-the-badge)

</div>

---

## Project Overview

The **KEC R2024 Academic Evaluation System** is a database-driven Java application developed to organize essential academic information in one place. It connects to a MySQL database through **Java Database Connectivity (JDBC)** and provides a menu-based interface for carrying out common academic operations.

The project demonstrates practical use of Java, relational database design, SQL operations, and JDBC connectivity in an academic management scenario.

> **Academic use:** This is a student project. Verify all evaluation formulas, attendance thresholds, condonation rules, and grade boundaries against the applicable official regulations before treating generated results as authoritative.

## Key Features

| Module | Capabilities |
|---|---|
| **Student Management** | Add and view student records, including roll number, name, department, and semester. |
| **Course Management** | Maintain course codes, titles, credits, and course types. |
| **Enrollment** | Associate students with courses through enrollment records. |
| **Attendance** | Record attendance information and support attendance-related reporting. |
| **Assessment Entry** | Maintain CIA 1, CIA 2, assignment, quiz, laboratory, and ESE marks. |
| **Examination Status** | Record ESE absence information when applicable. |
| **Semester Mark Sheet** | Retrieve assessment details and display available academic results. |
| **Attendance Defaulters** | Identify students below the attendance threshold configured in the application. |
| **Department Summary** | Display department-wise performance information supported by the implementation. |

## Technology Stack

| Technology | Purpose |
|---|---|
| **Java** | Application logic and menu-driven user interface |
| **MySQL 8** | Relational storage for academic records |
| **JDBC / MySQL Connector/J** | Communication between Java and MySQL |
| **Visual Studio Code** | Development and terminal-based execution |

## Project Structure

```text
KEC_R2024_Academic_System/
├── src/
│   ├── DBConnection.java
│   ├── StudentDAO.java
│   ├── AcademicDAO.java
│   ├── MainApp.java
│   └── mysql-connector-j-26.7.0.jar
├── out/                    # Compiled classes (generated)
├── kec_schema.sql          # Database schema, if included in your project
├── kec_sample_data.sql     # Sample data, if included in your project
├── README.md
└── TEST_CASES.txt
```

*The SQL filenames above are examples. Keep only filenames that actually exist in your repository. The Connector/J JAR path should match your current project setup.*

## Requirements

Before running the application, make sure you have:

- A Java Development Kit (JDK) installed.
- MySQL Server installed and running.
- MySQL Connector/J available in the project.
- Visual Studio Code or another terminal that can run Java commands.

Check Java installation from the VS Code terminal:

```powershell
java -version
javac -version
```

## Setup and Run on Windows

### 1. Open the project

Open the `KEC_R2024_Academic_System` folder in Visual Studio Code, then open **Terminal → New Terminal**.

### 2. Prepare the database

Connect to MySQL:

```powershell
mysql -u root -p
```

At the MySQL prompt, select the database:

```sql
CREATE DATABASE IF NOT EXISTS kec_academic;
USE kec_academic;
```

Run your project's SQL schema script if one is included. If the schema already creates the database and tables, follow that script's instructions rather than creating duplicate tables.

### 3. Configure the database connection

Open `src/DBConnection.java` and confirm that the JDBC URL, database name, username, and password match your local MySQL setup.

Example JDBC URL:

```java
jdbc:mysql://localhost:3306/kec_academic
```

**Security note:** Never commit your real database password to GitHub. Use a local-only configuration or environment variables for credentials, and keep private configuration files out of version control.

### 4. Compile the Java source files

Run the following command from the project root in the VS Code PowerShell terminal. It uses the Connector/J JAR path from the current project setup.

```powershell
javac -cp ".;src\mysql-connector-j-26.7.0.jar" -d out src\DBConnection.java src\StudentDAO.java src\AcademicDAO.java src\MainApp.java
```

### 5. Launch the application

```powershell
java -cp ".;out;src\mysql-connector-j-26.7.0.jar" MainApp
```

The main menu should appear in the terminal. Enter the number corresponding to the operation you want to perform.

## Database Overview

The application uses MySQL to persist academic information. The principal entities include:

- **Students** — student identity and academic details.
- **Courses** — course identifiers, titles, credits, and types.
- **Enrollments** — links between students and courses.
- **Assessment Marks** — internal assessment, assignment, quiz, laboratory, and ESE results, including ESE absence status.

The exact columns, constraints, and relationships are defined by the SQL schema used by your project. Refer to that schema for the authoritative database structure.

## Testing Checklist

Use `TEST_CASES.txt` if it is present in the repository. Useful manual checks include:

- Add a student and verify that the record is stored.
- Add a course and confirm it appears in the course list.
- Enroll a student in a course.
- Enter assessment marks and verify that the saved values can be retrieved.
- Check mark sheet generation using known test data.
- Test attendance reporting with records on both sides of the configured threshold.
- Test ESE absence handling, where supported.
- Restart the application and verify that saved records remain in MySQL.

## Troubleshooting

| Problem | What to check |
|---|---|
| `mysql` is not recognized | Add the MySQL `bin` directory to `PATH`, or run `mysql.exe` using its full path. |
| `No suitable driver` | Confirm that the Connector/J JAR exists and is included in the classpath. |
| `Communications link failure` | Check that MySQL Server is running and the host and port are correct. |
| `Access denied` | Verify the MySQL username and password configured in `DBConnection.java`. |
| `Unknown database 'kec_academic'` | Create the database and execute the required schema script. |
| `ClassNotFoundException: MainApp` | Compile successfully and ensure `out` is included in the runtime classpath. |
| Table or column errors | Confirm that the database schema matches the SQL statements used by the Java source files. |

## Potential Enhancements

- Add secure login and role-based access.
- Introduce a graphical or web-based interface.
- Export mark sheets and reports to PDF or Excel.
- Add automated unit and integration tests.
- Move database credentials to environment-based configuration.
- Improve validation and error messages for user input.

## Author

**Sahana S**  
B.Tech — Information Technology  
Kongu Engineering College

- **GitHub:** [Sahana-2373](https://github.com/Sahana-2373)
- **LeetCode:** [sahana_S2373](https://leetcode.com/sahana_S2373/)

---

<div align="center">

*Developed as an academic project to practice Java programming, JDBC connectivity, SQL, and database-backed application development.*

</div>
