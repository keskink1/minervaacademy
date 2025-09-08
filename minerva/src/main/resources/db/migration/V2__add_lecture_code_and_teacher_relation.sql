
ALTER TABLE lessons
    ADD COLUMN lecture_code VARCHAR(50) NOT NULL UNIQUE,
    ADD COLUMN teacher_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_lessons_teacher FOREIGN KEY (teacher_id)
        REFERENCES teachers(id)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE lessons
    MODIFY description TEXT NOT NULL;

DROP TABLE IF EXISTS teacher_lessons;