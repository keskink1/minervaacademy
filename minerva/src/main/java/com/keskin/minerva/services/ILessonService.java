package com.keskin.minerva.services;

import com.keskin.minerva.dtos.lessons.LessonDto;
import com.keskin.minerva.dtos.lessons.CreateLessonRequestDto;

import java.util.List;
import java.util.Set;

public interface ILessonService {

    List<LessonDto> getAllLessons();

    LessonDto getLessonById(Long id);

    LessonDto createLesson(CreateLessonRequestDto requestDto);

    void deleteLesson(Long id);

    Set<LessonDto> getLessonsOfStudent(Long studentId);

    Set<LessonDto> getLessonsOfTeacher(Long teacherId);

}
