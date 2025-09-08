package com.keskin.minerva.dtos.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keskin.minerva.entities.Role;
import lombok.Data;


@Data
public class UserDto {
    @JsonIgnore
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
