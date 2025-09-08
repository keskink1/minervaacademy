package com.keskin.minerva.dtos.students;

import com.keskin.minerva.dtos.lessons.LessonDto;
import com.keskin.minerva.dtos.users.UserDto;
import com.keskin.minerva.entities.Role;
import com.keskin.minerva.entities.TeacherNote;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto extends UserDto {

    private String enrollmentNumber;
    private LocalDate dateOfBirth;
    private Map<Long, TeacherNote> teacherNotes;
    private Set<LessonDto> lessons = new HashSet<>();
}
