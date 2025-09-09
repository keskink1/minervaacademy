package com.keskin.minerva.services.impl;

import com.keskin.minerva.dtos.students.StudentDto;
import com.keskin.minerva.dtos.students.UpdateStudentRequestDto;
import com.keskin.minerva.entities.Student;
import com.keskin.minerva.entities.TeacherNote;
import com.keskin.minerva.exceptions.ResourceNotFoundException;
import com.keskin.minerva.exceptions.TimeConflictException;
import com.keskin.minerva.mappers.StudentMapper;
import com.keskin.minerva.repositories.LessonRepository;
import com.keskin.minerva.repositories.StudentRepository;
import com.keskin.minerva.repositories.TeacherRepository;
import com.keskin.minerva.services.ILessonService;
import com.keskin.minerva.services.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements IStudentService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;
    private final StudentMapper studentMapper;
    private final ILessonService lessonService;

    private Student findByStudentId(Long id) {
        return studentRepository.findByIdWithDetails(id).orElseThrow(() ->
                new ResourceNotFoundException("Student", "ID", id.toString()));
    }


    @Override
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::entityToDto)
                .toList();
    }

    @Override
    public List<StudentDto> getAllActiveStudents() {
        return studentRepository.findByEnabledTrue()
                .stream()
                .map(studentMapper::entityToDto)
                .toList();
    }

    @Override
    public StudentDto getStudentById(Long id) {
        var student = findByStudentId(id);

        return studentMapper.entityToDto(student);
    }

    @Override
    public StudentDto assignLesson(Long studentId, Long lessonId) {
        var lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson", "Lesson ID", lessonId.toString()));

        Student student = findByStudentId(studentId);

        if (lessonService.hasTimeConflictForStudent(student, lesson)) {
            throw new TimeConflictException(
                    "Student already has a lesson at this time: " + lesson.getName()
            );
        }

        student.assignLesson(lesson);

        studentRepository.save(student);
        lessonRepository.save(lesson);

        return studentMapper.entityToDto(student);
    }


    @Override
    public StudentDto updateStudentById(UpdateStudentRequestDto request, Long id) {
        var student = findByStudentId(id);

        var updatedStudent = studentMapper.updateDtoToEntity(request, student);
        studentRepository.save(updatedStudent);

        return studentMapper.entityToDto(updatedStudent);
    }

    @Override
    public StudentDto freezeStudent(Long id) {
        var student = findByStudentId(id);

        student.freezeStudent();

        studentRepository.save(student);

        return studentMapper.entityToDto(student);
    }

    @Override
    public void deleteStudent(Long id) {
        var student = findByStudentId(id);
        studentRepository.delete(student);
    }

    @Override
    public void addTeacherNote(Long studentId, TeacherNote note) {
        var student = findByStudentId(studentId);
        student.addTeacherNote(note);
        studentRepository.save(student);
    }

    @Override
    public void deleteTeacherNote(Long studentId, Long teacherId) {
        var student = findByStudentId(studentId);
        student.removeTeacherNote(teacherId);

        if (!teacherRepository.existsById(teacherId)) {
            throw new ResourceNotFoundException("Teacher", "ID", teacherId.toString());
        }

        studentRepository.save(student);
    }

    @Override
    public Map<Long, TeacherNote> getAllTeacherNotes(Long studentId) {
        var student = findByStudentId(studentId);
        return student.getAllTeacherNotes();
    }


    @Override
    public void removeLesson(Long studentId, Long lessonId) {
        var student = findByStudentId(studentId);
        var lesson = lessonRepository.findById(lessonId).orElseThrow(() ->
                new ResourceNotFoundException("Lesson", "Lesson ID", lessonId.toString()));

        student.removeLesson(lesson);
        studentRepository.save(student);
    }


}
