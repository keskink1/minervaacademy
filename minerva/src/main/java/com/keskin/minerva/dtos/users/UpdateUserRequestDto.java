package com.keskin.minerva.dtos.users;


import com.keskin.minerva.entities.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDto {

    @Email(message = "Please provide a valid email address")
    private String email; // optional

    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).*$",
            message = "Password must contain at least one uppercase letter and one special character"
    )
    private String password;

    @Size(min = 3, message = "First name must be at least 3 characters long")
    private String firstName;

    @Size(min = 3, message = "Last name must be at least 3 characters long")
    private String lastName;

    @NotEmpty(message = "At least one role must be assigned")
    private Set<Role> roles;
}
