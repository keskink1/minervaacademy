package com.keskin.minerva.services;

import com.keskin.minerva.dtos.lessons.LessonDto;
import com.keskin.minerva.dtos.students.StudentDto;
import com.keskin.minerva.dtos.students.UpdateStudentRequestDto;
import com.keskin.minerva.entities.Student;
import com.keskin.minerva.entities.TeacherNote;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IStudentService {

    List<StudentDto> getAllStudents();

    List<StudentDto> getAllActiveStudents();

    StudentDto getStudentById(Long id);

    StudentDto assignLesson(Long studentId, Long lessonId);

    StudentDto updateStudentById(UpdateStudentRequestDto request, Long id);

    StudentDto freezeStudent(Long id);

    void deleteStudent(Long id);

    void addTeacherNote(Long studentId, TeacherNote note);

    void deleteTeacherNote(Long studentId, Long teacherId);

    Map<Long, TeacherNote> getAllTeacherNotes(Long studentId);

    void removeLesson(Long studentId, Long lessonId);

}
