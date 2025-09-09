package com.keskin.minerva.services.impl;

import com.keskin.minerva.dtos.lessons.LessonDto;
import com.keskin.minerva.dtos.lessons.CreateLessonRequestDto;
import com.keskin.minerva.entities.Lesson;
import com.keskin.minerva.entities.Student;
import com.keskin.minerva.entities.Teacher;
import com.keskin.minerva.exceptions.ResourceAlreadyExistsException;
import com.keskin.minerva.exceptions.ResourceNotFoundException;
import com.keskin.minerva.mappers.LessonMapper;
import com.keskin.minerva.repositories.LessonRepository;
import com.keskin.minerva.repositories.StudentRepository;
import com.keskin.minerva.services.ILessonService;
import com.keskin.minerva.services.ISecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonServiceImpl implements ILessonService {

    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final LessonMapper lessonMapper;
    private final ISecurityService securityService;

    private Lesson findLessonById(Long id){
        return lessonRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Lesson", "ID", id.toString()));
    }

    public boolean hasTimeConflictForStudent(Student student, Lesson newLesson) {
        return student.getLessons()
                .stream()
                .anyMatch(existing -> isConflict(existing, newLesson));
    }

    public boolean hasTimeConflictForTeacher(Teacher teacher, Lesson newLesson) {
        return teacher.getLessons()
                .stream()
                .anyMatch(existing -> isConflict(existing, newLesson));
    }

    private boolean isConflict(Lesson existing, Lesson candidate) {
        return candidate.getStartTime().isBefore(existing.getEndTime()) &&
                candidate.getEndTime().isAfter(existing.getStartTime());
    }

    @Override
    public List<LessonDto> getAllLessons() {
        return lessonRepository.findAll()
                .stream()
                .map(lessonMapper::entityToDto)
                .toList();
    }

    @Override
    public LessonDto getLessonById(Long id) {
        var lesson = findLessonById(id);
        return lessonMapper.entityToDto(lesson);
    }

    @Override
    public LessonDto createLesson(CreateLessonRequestDto requestDto) {
        var lectureCode = requestDto.getLectureCode();
        if (lessonRepository.findByLectureCode(lectureCode).isPresent()) {
            throw new ResourceAlreadyExistsException("Lecture code", lectureCode);
        }

        var newLesson = lessonMapper.createRequestToEntity(requestDto);

        newLesson.setCreatedAt(LocalDateTime.now());
        newLesson.setStartTime(requestDto.getStartTime());
        newLesson.setEndTime(requestDto.getEndTime());

        lessonRepository.save(newLesson);

        return lessonMapper.entityToDto(newLesson);
    }


    @Override
    public void deleteLesson(Long id) {
        var lesson = findLessonById(id);
        lessonRepository.delete(lesson);
    }

    @Override
    public Set<LessonDto> getLessonsOfStudent(Long studentId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(auth.getPrincipal().toString());

        if (!securityService.currentUserHasRoleAdmin() && !userId.equals(studentId)) {
            try {
                throw new AccessDeniedException("Cannot view other student's lessons");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId.toString()));

        return student.getLessons()
                .stream()
                .map(lessonMapper::entityToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<LessonDto> getLessonsOfTeacher(Long teacherId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(auth.getPrincipal().toString());

        if (!securityService.currentUserHasRoleAdmin() && !userId.equals(teacherId)) {
            try {
                throw new AccessDeniedException("Cannot view other student's lessons");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

        var teacher = studentRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId.toString()));

        return teacher.getLessons()
                .stream()
                .map(lessonMapper::entityToDto)
                .collect(Collectors.toSet());
    }
}
