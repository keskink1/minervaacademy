package com.keskin.minerva.dtos.users;

import com.keskin.minerva.entities.Lesson;
import com.keskin.minerva.entities.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDto {

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$",
            message = "Password must contain at least one uppercase letter and one special character"
    )
    private String password;

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 3, message = "First name must be at least 3 characters long")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 3, message = "Last name must be at least 3 characters long")
    private String lastName;

    private Set<@Valid Lesson> lessons = new HashSet<>();

    @NotEmpty(message = "At least one role must be assigned")
    private Set<Role> roles = new HashSet<>();
}
