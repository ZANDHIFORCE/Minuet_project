INSERT INTO teacher (name, subject) VALUES ('미서', '피아노');
INSERT INTO teacher (name, subject) VALUES ('동휘', '컴퓨터');

INSERT INTO student (name, progressSessions, totalSessions) VALUES ('동서', 5, 10);
INSERT INTO student (name, progressSessions, totalSessions) VALUES ('미휘', 3, 8);
INSERT INTO student (name, progressSessions, totalSessions) VALUES ('휘동', 7, 15);

INSERT INTO lesson_info (teacher_id, student_id, date_time, completed) VALUES (1, 1, '2025-08-15 10:00:00', FALSE);
INSERT INTO lesson_info (teacher_id, student_id, date_time, completed) VALUES (2, 2, '2025-08-16 14:30:00', TRUE);
INSERT INTO lesson_info (teacher_id, student_id, date_time, completed) VALUES (1, 3, '2025-08-17 11:00:00', FALSE);