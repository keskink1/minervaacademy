package com.keskin.minerva.services;

import com.keskin.minerva.entities.Lesson;
import com.keskin.minerva.entities.Student;
import com.keskin.minerva.entities.Teacher;


public interface IScheduleService {
    boolean hasTimeConflictForStudent(Student student, Lesson newLesson);
    boolean hasTimeConflictForTeacher(Teacher teacher, Lesson newLesson);
}
