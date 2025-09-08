package com.keskin.minerva.entities;

import com.keskin.minerva.exceptions.ResourceAlreadyExistsException;
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

    @OneToOne(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private WeeklySchedule weeklySchedule;


    public void freezeTeacher() {
        setEnabled(false);
    }

    public void assignLesson(Lesson lesson) {
        if (lesson.getTeacher() == null) {
            this.lessons.add(lesson);
            lesson.setTeacher(this);
        } else {
            throw new ResourceAlreadyExistsException("Teacher", lesson.toString());
        }
    }

    public void removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        if (lesson.getTeacher() == this) {
            lesson.setTeacher(null);
        }
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