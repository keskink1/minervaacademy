package com.keskin.minerva.dtos.teachers;


import com.keskin.minerva.dtos.users.UserDto;
import com.keskin.minerva.entities.Role;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto extends UserDto {
    private String phoneNumber;
}
