package com.keskin.minerva.services.impl;

import com.keskin.minerva.dtos.teachers.TeacherDto;
import com.keskin.minerva.dtos.teachers.TeacherLessonDto;
import com.keskin.minerva.dtos.teachers.UpdateTeacherRequestDto;
import com.keskin.minerva.entities.Teacher;
import com.keskin.minerva.exceptions.ResourceNotFoundException;
import com.keskin.minerva.exceptions.TimeConflictException;
import com.keskin.minerva.mappers.TeacherMapper;
import com.keskin.minerva.repositories.LessonRepository;
import com.keskin.minerva.repositories.TeacherRepository;
import com.keskin.minerva.services.ILessonService;
import com.keskin.minerva.services.ITeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherServiceImpl implements ITeacherService {

    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;
    private final ILessonService lessonService;

    private Teacher findByTeacherId(Long id) {
        return teacherRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Teacher", "ID", id.toString()));
    }

    @Override
    public List<TeacherDto> getAllTeachers() {
        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::entityToDto)
                .toList();
    }

    @Override
    public List<TeacherDto> getAllActiveTeachers() {
        return teacherRepository.findByEnabledTrue()
                .stream()
                .map(teacherMapper::entityToDto)
                .toList();
    }

    @Override
    public Set<TeacherLessonDto> getTeacherLessonsById(Long id) {
        var teacher = findByTeacherId(id);
        return teacher.getLessons()
                .stream()
                .map(l -> teacherMapper.entityToTeacherLessonDto(teacher))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public TeacherDto assignLesson(Long teacherId, Long lessonId) {
        var lesson = lessonRepository.findById(lessonId).orElseThrow(() ->
                new ResourceNotFoundException("Lesson", "Lesson ID", lessonId.toString()));

        Teacher teacher = findByTeacherId(teacherId);

        if(lessonService.hasTimeConflictForTeacher(teacher, lesson)){
            throw new TimeConflictException(
                    "Teacher already has a lesson at this time: " + lesson.getName()
            );
        }

        teacher.assignLesson(lesson);

        teacherRepository.saveAndFlush(teacher);
        lessonRepository.saveAndFlush(lesson);

        return teacherMapper.entityToDto(teacher);
    }


    @Override
    public TeacherDto updateTeacherById(UpdateTeacherRequestDto request, Long id) {
        var teacher = findByTeacherId(id);
        var updatedTeacher = teacherMapper.updateDtoToEntity(request, teacher);

        teacherRepository.save(updatedTeacher);

        return teacherMapper.entityToDto(updatedTeacher);
    }

    @Override
    public TeacherDto freezeTeacher(Long id) {
        var teacher = findByTeacherId(id);
        teacher.freezeTeacher();

        var frozenTeacher = teacherRepository.save(teacher);

        return teacherMapper.entityToDto(frozenTeacher);
    }

    @Override
    public void deleteTeacher(Long id) {
        var teacher = findByTeacherId(id);
        teacherRepository.delete(teacher);
    }

    @Override
    public void removeLesson(Long teacherId, Long lessonId) {
        var teacher = findByTeacherId(teacherId);
        var lesson = lessonRepository.findById(lessonId).orElseThrow(() ->
                new ResourceNotFoundException("Lesson", "Lesson ID", lessonId.toString()));

        teacher.removeLesson(lesson);
        teacherRepository.save(teacher);
    }
}
