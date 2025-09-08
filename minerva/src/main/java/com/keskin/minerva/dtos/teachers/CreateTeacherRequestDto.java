package com.keskin.minerva.dtos.teachers;

import com.keskin.minerva.dtos.users.CreateUserRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeacherRequestDto extends CreateUserRequestDto {
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\+34\\d{9}$", message = "Phone number must start with +34 followed by 9 digits")
    private String phoneNumber;

}
