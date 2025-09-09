package com.keskin.minerva.dtos.lessons;

import com.keskin.minerva.entities.Student;
import com.keskin.minerva.entities.Teacher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateLessonRequestDto {

    @NotBlank(message = "Lesson name cannot be blank")
    @Size(max = 255, message = "Lesson name cannot exceed 255 characters")
    private String name;

    @NotBlank(message = "Lecture code cannot be blank")
    @Size(max = 50, message = "Lecture code cannot exceed 50 characters")
    private String lectureCode;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Long teacherId;

    private Set<Long> studentIds;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

}
