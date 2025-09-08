CREATE TABLE weekly_schedules (
                                  id BIGINT NOT NULL AUTO_INCREMENT,
                                  student_id BIGINT NULL,
                                  teacher_id BIGINT NULL,
                                  PRIMARY KEY (id),
                                  CONSTRAINT fk_weekly_schedule_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE ON UPDATE CASCADE,
                                  CONSTRAINT fk_weekly_schedule_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE weekly_schedule_lessons (
                                         weekly_schedule_id BIGINT NOT NULL,
                                         lesson_id BIGINT NOT NULL,
                                         PRIMARY KEY (weekly_schedule_id, lesson_id),
                                         FOREIGN KEY (weekly_schedule_id) REFERENCES weekly_schedules(id) ON DELETE CASCADE ON UPDATE CASCADE,
                                         FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
