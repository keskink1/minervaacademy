package com.keskin.minerva.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherNote {
    private Long teacherId;
    private String note;
    private LocalDateTime timestamp;
}