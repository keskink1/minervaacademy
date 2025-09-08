package com.keskin.minerva.repositories;

import com.keskin.minerva.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("SELECT s FROM Teacher s JOIN s.lessons l WHERE l.id = ?1")
    List<Teacher> findTeachersByLessons(Long lessonId);

    List<Teacher> findByEnabledTrue();

    Optional<Object> findByEmail(String email);

    Optional<Object> findByPhoneNumber(String phoneNumber);
}