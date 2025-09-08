package com.keskin.minerva.dtos.students;

import com.keskin.minerva.dtos.users.CreateUserRequestDto;
import com.keskin.minerva.utils.BirthDateConstraint;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudentRequestDto extends CreateUserRequestDto {
    @NotBlank(message = "Enrollment number cannot be blank")
    @Size(max = 10, message = "Enrollment number cannot exceed 10 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Enrollment number can contain only letters and digits")
    private String enrollmentNumber;

    @NotNull(message = "Date of birth cannot be null")
    @PastOrPresent(message = "Date of birth cannot be in the future")
    @BirthDateConstraint
    private LocalDate dateOfBirth;


}
