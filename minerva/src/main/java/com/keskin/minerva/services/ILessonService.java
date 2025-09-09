package com.keskin.minerva.services;

import com.keskin.minerva.dtos.lessons.LessonDto;
import com.keskin.minerva.dtos.lessons.CreateLessonRequestDto;
import com.keskin.minerva.entities.Lesson;
import com.keskin.minerva.entities.Student;
import com.keskin.minerva.entities.Teacher;

import java.util.List;
import java.util.Set;

public interface ILessonService {

    boolean hasTimeConflictForStudent(Student student, Lesson newLesson);

    boolean hasTimeConflictForTeacher(Teacher teacher, Lesson newLesson);

    List<LessonDto> getAllLessons();

    LessonDto getLessonById(Long id);

    LessonDto createLesson(CreateLessonRequestDto requestDto);

    void deleteLesson(Long id);

    Set<LessonDto> getLessonsOfStudent(Long studentId);

    Set<LessonDto> getLessonsOfTeacher(Long teacherId);

}
