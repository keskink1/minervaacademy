package com.keskin.minerva.dtos.teachers;

import com.keskin.minerva.dtos.lessons.LessonDto;
import com.keskin.minerva.entities.Lesson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherLessonDto {
    private String teacherName;
    private Set<LessonDto> lessons;
}
