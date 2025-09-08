package com.keskin.minerva.services;

import com.keskin.minerva.dtos.lessons.LessonDto;
import com.keskin.minerva.dtos.teachers.TeacherDto;
import com.keskin.minerva.dtos.teachers.TeacherLessonDto;
import com.keskin.minerva.dtos.teachers.UpdateTeacherRequestDto;
import com.keskin.minerva.entities.Teacher;

import java.util.List;
import java.util.Set;

public interface ITeacherService {

    List<TeacherDto> getAllTeachers();

    List<TeacherDto> getAllActiveTeachers();

    TeacherDto assignLesson(Long teacherId, Long lessonId);

    Set<TeacherLessonDto> getTeacherLessonsById(Long id);

    TeacherDto updateTeacherById(UpdateTeacherRequestDto request, Long id);

    TeacherDto freezeTeacher(Long id);

    void deleteTeacher(Long id);

    void removeLesson(Long teacherId, Long lessonId);

}
