package com.keskin.minerva.dtos.students;

import com.keskin.minerva.entities.Lesson;
import com.keskin.minerva.utils.BirthDateConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentRequestDto {
    @NotBlank(message = "Enrollment number cannot be blank")
    @Size(max = 10, message = "Enrollment number cannot exceed 10 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Enrollment number can contain only letters and digits")
    private String enrollmentNumber;

    @NotNull(message = "Date of birth cannot be null")
    @PastOrPresent(message = "Date of birth cannot be in the future")
    @BirthDateConstraint
    private LocalDate dateOfBirth;

    private Set<@Valid Lesson> lessons = new HashSet<>();
}
