package com.keskin.minerva.entities;

import com.keskin.minerva.exceptions.ResourceAlreadyExistsException;
import com.keskin.minerva.exceptions.TimeConflictException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Table(name = "teachers")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends AppUser {

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Lesson> lessons = new HashSet<>();


    public void freezeTeacher() {
        setEnabled(false);
    }

    public void assignLesson(Lesson lesson) {
        if (lesson.getTeacher() == null) {
            lesson.setTeacher(this);
            this.lessons.add(lesson);
        } else {
            throw new TimeConflictException(
                    "Teacher already has a lesson at this time: " + lesson.getName()
            );
        }
    }


    public void removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        if (lesson.getTeacher() == this) {
            lesson.setTeacher(null);
        }
    }

    public int calculateWeeklyHours() {
        if (lessons == null || lessons.isEmpty()) {
            return 0;
        }

        return lessons.stream()
                .mapToInt(Lesson::getDuration)
                .sum();
    }

}