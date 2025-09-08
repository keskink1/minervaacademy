package com.keskin.minerva.repositories;

import com.keskin.minerva.entities.Student;
import com.keskin.minerva.entities.TeacherNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s JOIN s.lessons l WHERE l.id = ?1")
    List<Student> findStudentsByLessons(Long lessonId);

    List<Student> findByEnabledTrue();

    @Query("SELECT s.teacherNotes FROM Student s WHERE s.id = ?1")
    Map<Long, TeacherNote> findTeacherNotes(Long studentId);

    Optional<Student> findByEmail(String email);

    boolean findByEnrollmentNumber(String enrollmentNumber);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.lessons WHERE s.id = :id")
    Optional<Student> findByIdWithDetails(@Param("id") Long id);
}