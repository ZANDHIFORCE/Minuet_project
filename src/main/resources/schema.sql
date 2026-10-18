CREATE TABLE teacher (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    subject VARCHAR(100) NOT NULL
);

CREATE TABLE student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    progressSessions INT NOT NULL DEFAULT 0,
    totalSessions INT NOT NULL DEFAULT 0
);

CREATE TABLE lesson_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    date_time TIMESTAMP NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (teacher_id) REFERENCES teacher(id),
    FOREIGN KEY (student_id) REFERENCES student(id)
);

CREATE TABLE lesson_slot (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    lesson_day INT NOT NULL CHECK (lesson_day BETWEEN 0 AND 6),
    lesson_time TIME NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES teacher(id),
    FOREIGN KEY (student_id) REFERENCES student(id),
    CONSTRAINT uq_lesson_slot_teacher UNIQUE (teacher_id, lesson_day, lesson_time),
    CONSTRAINT uq_lesson_slot_student UNIQUE (student_id, lesson_day, lesson_time)
);
