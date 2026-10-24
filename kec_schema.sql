CREATE DATABASE IF NOT EXISTS kec_academic;
USE kec_academic;

CREATE TABLE IF NOT EXISTS students (
    roll_no VARCHAR(20) PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    department VARCHAR(20) NOT NULL,
    current_semester TINYINT NOT NULL,
    CHECK (current_semester BETWEEN 1 AND 8)
);

CREATE TABLE IF NOT EXISTS courses (
    course_code VARCHAR(20) PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    credits DECIMAL(4,1) NOT NULL,
    course_type ENUM('THEORY','PRACTICAL','EMBEDDED') NOT NULL,
    CHECK (credits > 0)
);

CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    roll_no VARCHAR(20) NOT NULL,
    course_code VARCHAR(20) NOT NULL,
    semester TINYINT NOT NULL,
    classes_held INT NOT NULL DEFAULT 0,
    classes_attended INT NOT NULL DEFAULT 0,
    medical_condonation_approved BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE KEY uq_student_course_semester (roll_no, course_code, semester),
    CONSTRAINT fk_enrollment_student FOREIGN KEY (roll_no)
        REFERENCES students(roll_no) ON DELETE CASCADE,
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_code)
        REFERENCES courses(course_code) ON DELETE CASCADE,
    CHECK (semester BETWEEN 1 AND 8),
    CHECK (classes_held >= 0),
    CHECK (classes_attended >= 0 AND classes_attended <= classes_held)
);

CREATE TABLE IF NOT EXISTS assessment_marks (
    enrollment_id BIGINT PRIMARY KEY,
    cia1 DECIMAL(5,2) NULL,
    cia2 DECIMAL(5,2) NULL,
    assignment_mark DECIMAL(5,2) NULL,
    quiz_mark DECIMAL(5,2) NULL,
    lab_mark DECIMAL(5,2) NULL,
    ese_mark DECIMAL(5,2) NULL,
    ese_absent BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_marks_enrollment FOREIGN KEY (enrollment_id)
        REFERENCES enrollments(enrollment_id) ON DELETE CASCADE,
    CHECK (cia1 IS NULL OR cia1 BETWEEN 0 AND 100),
    CHECK (cia2 IS NULL OR cia2 BETWEEN 0 AND 100),
    CHECK (assignment_mark IS NULL OR assignment_mark BETWEEN 0 AND 100),
    CHECK (quiz_mark IS NULL OR quiz_mark BETWEEN 0 AND 100),
    CHECK (lab_mark IS NULL OR lab_mark BETWEEN 0 AND 100),
    CHECK (ese_mark IS NULL OR ese_mark BETWEEN 0 AND 100)
);

-- Optional configurable CIA component percentages are intentionally not
-- stored here: this prototype uses an explicit, documented average rule.
