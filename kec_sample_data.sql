USE kec_academic;

INSERT INTO students(roll_no, student_name, department, current_semester) VALUES
('24ITR001','Arun Kumar','IT',3),
('24ITR002','Meena Devi','IT',3)
ON DUPLICATE KEY UPDATE student_name=VALUES(student_name);

INSERT INTO courses(course_code, title, credits, course_type) VALUES
('24ITR31','Database Management Systems',3,'THEORY'),
('24ITR32','Java Programming Laboratory',2,'PRACTICAL'),
('24ITR33','Data Analytics Fundamentals',3,'EMBEDDED')
ON DUPLICATE KEY UPDATE title=VALUES(title);

INSERT INTO enrollments(roll_no, course_code, semester, classes_held, classes_attended, medical_condonation_approved)
VALUES
('24ITR001','24ITR31',3,40,34,FALSE),
('24ITR001','24ITR32',3,40,36,FALSE),
('24ITR001','24ITR33',3,40,32,FALSE),
('24ITR002','24ITR31',3,40,24,FALSE)
ON DUPLICATE KEY UPDATE classes_held=VALUES(classes_held), classes_attended=VALUES(classes_attended);

INSERT INTO assessment_marks(enrollment_id, cia1, cia2, assignment_mark, quiz_mark, lab_mark, ese_mark, ese_absent)
SELECT enrollment_id, 82, 78, 85, 80, NULL, 76, FALSE FROM enrollments
WHERE roll_no='24ITR001' AND course_code='24ITR31' AND semester=3
ON DUPLICATE KEY UPDATE cia1=VALUES(cia1), cia2=VALUES(cia2), assignment_mark=VALUES(assignment_mark), quiz_mark=VALUES(quiz_mark), ese_mark=VALUES(ese_mark);

INSERT INTO assessment_marks(enrollment_id, cia1, cia2, assignment_mark, quiz_mark, lab_mark, ese_mark, ese_absent)
SELECT enrollment_id, 90, 88, NULL, NULL, 92, 86, FALSE FROM enrollments
WHERE roll_no='24ITR001' AND course_code='24ITR32' AND semester=3
ON DUPLICATE KEY UPDATE cia1=VALUES(cia1), cia2=VALUES(cia2), lab_mark=VALUES(lab_mark), ese_mark=VALUES(ese_mark);

INSERT INTO assessment_marks(enrollment_id, cia1, cia2, assignment_mark, quiz_mark, lab_mark, ese_mark, ese_absent)
SELECT enrollment_id, 75, 80, 82, 79, 84, 73, FALSE FROM enrollments
WHERE roll_no='24ITR001' AND course_code='24ITR33' AND semester=3
ON DUPLICATE KEY UPDATE cia1=VALUES(cia1), cia2=VALUES(cia2), assignment_mark=VALUES(assignment_mark), quiz_mark=VALUES(quiz_mark), lab_mark=VALUES(lab_mark), ese_mark=VALUES(ese_mark);

INSERT INTO assessment_marks(enrollment_id, cia1, cia2, assignment_mark, quiz_mark, lab_mark, ese_mark, ese_absent)
SELECT enrollment_id, 60, 55, 62, 58, NULL, 40, FALSE FROM enrollments
WHERE roll_no='24ITR002' AND course_code='24ITR31' AND semester=3
ON DUPLICATE KEY UPDATE cia1=VALUES(cia1), cia2=VALUES(cia2), assignment_mark=VALUES(assignment_mark), quiz_mark=VALUES(quiz_mark), ese_mark=VALUES(ese_mark);
