package com.keskin.minerva.dtos.teachers;

import com.keskin.minerva.entities.Lesson;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTeacherRequestDto {

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\+34\\d{9}$", message = "Phone number must start with +34 followed by 9 digits")
    private String phoneNumber;

    private Set<@Valid Lesson> lessons;
}
