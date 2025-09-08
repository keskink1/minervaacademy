package com.keskin.minerva.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.keskin.minerva.utils.TeacherNotesConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Table(name = "students")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends AppUser {

    @Column(name = "enrollment_number", unique = true)
    private String enrollmentNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Convert(converter = TeacherNotesConverter.class)
    @Column(name = "teacher_notes", columnDefinition = "JSON")
    private Map<Long, TeacherNote> teacherNotes = new HashMap<>();


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "student_lessons",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    private Set<Lesson> lessons = new HashSet<>();

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private WeeklySchedule weeklySchedule;

    public void freezeStudent() {
        setEnabled(false);
    }

    public void assignLesson(Lesson lesson) {
        this.lessons.add(lesson);
        lesson.getStudents().add(this);
    }

    public void removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        lesson.getStudents().remove(this);
    }

    public void addTeacherNote(TeacherNote note) {
        this.teacherNotes.put(note.getTeacherId(), note);
    }

    public void removeTeacherNote(Long teacherId) {
        this.teacherNotes.remove(teacherId);
    }

    public Map<Long, TeacherNote> getAllTeacherNotes() {
        return Collections.unmodifiableMap(this.teacherNotes);
    }

    public int calculateWeeklyHours() {
        if (weeklySchedule == null || weeklySchedule.getLessons() == null || weeklySchedule.getLessons().isEmpty()) {
            return 0;
        }

        return lessons.stream()
                .mapToInt(Lesson::getDuration)
                .sum();
    }

}