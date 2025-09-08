ALTER TABLE lessons
    ADD COLUMN start_time DATETIME NOT NULL AFTER duration,
ADD COLUMN end_time DATETIME NOT NULL AFTER start_time;
