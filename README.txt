KEC R2024 STUDENT ACADEMIC & CONTINUOUS EVALUATION SYSTEM
Java + JDBC + MySQL | Menu-driven console application

CONTENTS
- kec_schema.sql: creates database and four required tables
- kec_sample_data.sql: optional sample records for testing
- src/DBConnection.java: JDBC connection settings
- src/StudentDAO.java: student and course CRUD
- src/AcademicDAO.java: enrollment, attendance, marks and reports
- src/MainApp.java: menu-driven console interface

REQUIREMENTS
1. JDK 17 or newer (JDK 8+ may work, but JDK 17 is recommended).
2. MySQL Server 8.x and MySQL Workbench or MySQL Command Line Client.
3. MySQL Connector/J (JDBC driver JAR).
4. Command Prompt or VS Code.

STEP 1 — CHECK JAVA
Open Command Prompt and run:
  java -version
  javac -version
If either command is not found, install a JDK and configure PATH.

STEP 2 — CREATE THE DATABASE
Open MySQL Workbench, connect to your local MySQL server, open kec_schema.sql,
and click the lightning-bolt Execute button.
Alternatively, from a command prompt:
  mysql -u root -p < "C:\path\to\kec_schema.sql"

Optional test records:
  mysql -u root -p < "C:\path\to\kec_sample_data.sql"

STEP 3 — GET MYSQL CONNECTOR/J
Download MySQL Connector/J from the official MySQL website:
  https://dev.mysql.com/downloads/connector/j/
Choose the Platform Independent ZIP, extract it, and locate the JAR file,
for example mysql-connector-j-9.x.x.jar.
Do not put the JAR inside the src folder; keep it in the project root or a lib folder.

STEP 4 — SET DATABASE CREDENTIALS
DBConnection.java uses:
  URL: jdbc:mysql://localhost:3306/kec_academic?useSSL=false&serverTimezone=UTC
  USER: root
  PASSWORD: change_me
Edit PASSWORD in DBConnection.java to the password you use for MySQL.
For better practice, environment variables are also supported:
  KEC_DB_URL, KEC_DB_USER, KEC_DB_PASSWORD

STEP 5 — COMPILE AND RUN ON WINDOWS
Create a folder, for example C:\KEC_R2024_Academic_System.
Extract this ZIP into that folder. Put the Connector/J JAR at:
  C:\KEC_R2024_Academic_System\mysql-connector-j-9.x.x.jar
Open Command Prompt and move to the folder:
  cd C:\KEC_R2024_Academic_System

Compile:
  javac -cp ".;mysql-connector-j-9.x.x.jar" -d out src\DBConnection.java src\StudentDAO.java src\AcademicDAO.java src\MainApp.java

Run:
  java -cp ".;out;mysql-connector-j-9.x.x.jar" MainApp

IMPORTANT: Replace mysql-connector-j-9.x.x.jar with the exact filename you downloaded.
On Windows, the classpath separator is a semicolon (;).

MENU WORKFLOW
1. Add student.
2. Add course.
3. Enroll student in each course for the relevant semester.
4. Update attendance for each enrollment. Record condonation approval if applicable.
5. Phase 1: enter CIA Test 1 and CIA Test 2.
6. Phase 2: enter assignment, quiz and/or lab marks. Leave optional fields blank to skip them.
7. Phase 3: enter ESE mark or mark the student absent.
8. Generate the semester mark sheet, defaulter report or department summary.

EVALUATION ASSUMPTIONS IN THIS EDUCATIONAL PROTOTYPE
- CIA marks are each entered on a 0–100 scale.
- THEORY CIA raw score = average of entered CIA1, CIA2, assignment and quiz components.
- PRACTICAL CIA raw score = average of entered CIA1, CIA2 and lab components.
- EMBEDDED CIA raw score = average of entered CIA1, CIA2, assignment, quiz and lab components.
- Missing CIA components are ignored in the average; this permits phased entry. Therefore reports may be provisional before all CIA components are entered.
- THEORY: CIA 40%, ESE 60%; ESE minimum raw mark 45/100; total >= 50 to pass.
- PRACTICAL: CIA 60%, ESE 40%; ESE minimum raw mark 50/100; total >= 50 to pass.
- EMBEDDED: CIA 50%, ESE 50%; ESE minimum raw mark 45/100; total >= 50 to pass.
- Attendance <65% => SA. Attendance 65% to <75% without approved medical condonation is treated as SA / condonation pending in this prototype. With approval, the student can proceed.
- ESE absent => AB. Failed ESE minimum or total below 50 => RA.
- Grades: O 91–100 (10), A+ 81–90 (9), A 71–80 (8), B+ 61–70 (7), B 50–60 (6), RA/SA/AB 0.
- SGPA = sum(credits x grade points) / sum(enrolled credits). Pending/non-passing grades contribute zero grade points.
- The summary's pass/fail counts are enrollment/course counts. Pending, SA and AB are not counted as PASS; only explicit FAIL/RA is counted in FAIL.
- This is a student project implementation based on the supplied brief, not an official KEC-certified system. Verify exact CIA component conversion, grade boundaries, rounding, and condonation policy against the official R2024 regulations before claiming official compliance.

TROUBLESHOOTING
- "No suitable driver": Connector/J JAR missing from runtime classpath.
- "Access denied": username/password incorrect; update DBConnection.java or environment variables.
- "Unknown database": execute kec_schema.sql.
- "Communications link failure": start MySQL Server and verify port 3306.
- "Duplicate entry": that roll number, course code, or student-course-semester enrollment already exists.
- If SQL CHECK constraints behave differently on an older MySQL version, use MySQL 8.x.

SCREENSHOT EVIDENCE FOR SUBMISSION
Capture these with Windows Snipping Tool (Win+Shift+S):
1. MySQL Workbench showing successful execution of kec_schema.sql.
2. Console showing a student added.
3. Console showing a course added.
4. Console showing enrollment and attendance update.
5. Console showing CIA1/CIA2 phase entry, followed by Phase 2 and ESE entry.
6. Console showing the generated semester mark sheet with grade and SGPA.
7. Attendance defaulter report and department summary.

SAMPLE TEST
Use the optional sample SQL script, run the Java application, choose option 10,
enter roll number 24ITR001 and semester 3. Then try option 11 for attendance
defaulters and option 12 for the class summary.
