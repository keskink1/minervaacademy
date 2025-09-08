CREATE DATABASE IF NOT EXISTS anubis;
USE anubis;

CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       enabled BOOLEAN NOT NULL DEFAULT TRUE,
                       user_type VARCHAR(31) NOT NULL,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE students (
                          id BIGINT NOT NULL,
                          enrollment_number VARCHAR(255) UNIQUE,
                          date_of_birth DATE,
                          teacher_notes JSON,
                          PRIMARY KEY (id),
                          CONSTRAINT fk_students_user FOREIGN KEY (id) REFERENCES users(id)
                              ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE teachers (
                          id BIGINT NOT NULL,
                          phone_number VARCHAR(20),
                          PRIMARY KEY (id),
                          CONSTRAINT fk_teachers_user FOREIGN KEY (id) REFERENCES users(id)
                              ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE roles (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(50) NOT NULL UNIQUE,
                       PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id)
                                ON DELETE CASCADE ON UPDATE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES roles(id)
                                ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE lessons (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         name VARCHAR(255) NOT NULL UNIQUE,
                         description TEXT,
                         PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE teacher_lessons (
                                 teacher_id BIGINT NOT NULL,
                                 lesson_id BIGINT NOT NULL,
                                 PRIMARY KEY (teacher_id, lesson_id),
                                 FOREIGN KEY (teacher_id) REFERENCES teachers(id)
                                     ON DELETE CASCADE ON UPDATE CASCADE,
                                 FOREIGN KEY (lesson_id) REFERENCES lessons(id)
                                     ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE student_lessons (
                                 student_id BIGINT NOT NULL,
                                 lesson_id BIGINT NOT NULL,
                                 PRIMARY KEY (student_id, lesson_id),
                                 FOREIGN KEY (student_id) REFERENCES students(id)
                                     ON DELETE CASCADE ON UPDATE CASCADE,
                                 FOREIGN KEY (lesson_id) REFERENCES lessons(id)
                                     ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
