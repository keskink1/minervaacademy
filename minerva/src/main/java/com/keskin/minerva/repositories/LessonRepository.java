package com.keskin.minerva.repositories;

import com.keskin.minerva.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Optional<Lesson> findByLectureCode(String lectureCode);
}