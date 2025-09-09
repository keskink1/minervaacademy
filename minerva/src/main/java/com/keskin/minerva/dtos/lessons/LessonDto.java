package com.keskin.minerva.dtos.lessons;

import com.keskin.minerva.dtos.teachers.TeacherDto;
import lombok.*;


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
