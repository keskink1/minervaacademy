package com.keskin.minerva.services.impl;

import com.keskin.minerva.entities.Lesson;
import com.keskin.minerva.entities.Student;
import com.keskin.minerva.entities.Teacher;
import com.keskin.minerva.services.IScheduleService;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl implements IScheduleService {

    @Override
    public boolean hasTimeConflictForStudent(Student student, Lesson newLesson) {
        return student.getWeeklySchedule().getLessons()
                .stream()
                .anyMatch(existing -> isConflict(existing, newLesson));
    }

    @Override
    public boolean hasTimeConflictForTeacher(Teacher teacher, Lesson newLesson) {
        return teacher.getWeeklySchedule().getLessons()
                .stream()
                .anyMatch(existing -> isConflict(existing, newLesson));
    }

    private boolean isConflict(Lesson existing, Lesson candidate) {
        return candidate.getStartTime().isBefore(existing.getEndTime()) &&
                candidate.getEndTime().isAfter(existing.getStartTime());
    }
}
