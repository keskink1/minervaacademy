package com.keskin.minerva.dtos.lessons;

import com.keskin.minerva.dtos.students.StudentDto;
import com.keskin.minerva.dtos.teachers.TeacherDto;
import com.keskin.minerva.entities.Student;
import com.keskin.minerva.entities.Teacher;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonDto {
    private String name;

    private String lectureCode;

    private String description;

    private TeacherDto teacher;

}
