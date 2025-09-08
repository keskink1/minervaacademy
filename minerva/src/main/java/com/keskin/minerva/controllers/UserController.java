package com.keskin.minerva.controllers;


import com.keskin.minerva.dtos.students.CreateStudentRequestDto;
import com.keskin.minerva.dtos.students.StudentDto;
import com.keskin.minerva.dtos.students.UpdateStudentRequestDto;
import com.keskin.minerva.dtos.teachers.CreateTeacherRequestDto;
import com.keskin.minerva.dtos.teachers.TeacherDto;
import com.keskin.minerva.dtos.teachers.UpdateTeacherRequestDto;
import com.keskin.minerva.entities.AppUser;
import com.keskin.minerva.services.IStudentService;
import com.keskin.minerva.services.ITeacherService;
import com.keskin.minerva.services.IUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/users",  produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
@Validated
public class UserController {
    private final IUserService userService;
    private final IStudentService studentService;
    private final ITeacherService teacherService;


    @PostMapping("/register/teacher")
    public ResponseEntity<?> registerTeacher(
            @Valid @RequestBody CreateTeacherRequestDto request,
            UriComponentsBuilder uriBuilder
    ) {
        TeacherDto teacherDto = userService.createTeacher(request);

        var location = uriBuilder
                .path("/api/users/{id}")
                .buildAndExpand(teacherDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(teacherDto);
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(
            @Valid @RequestBody CreateStudentRequestDto request,
            UriComponentsBuilder uriBuilder
    ) {
        StudentDto studentDto = userService.createStudent(request);

        var location = uriBuilder
                .path("/api/users/{id}")
                .buildAndExpand(studentDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(studentDto);
    }

    @PutMapping("/students/me")
    public ResponseEntity<Object> updateMeStudent(@Valid @RequestBody UpdateStudentRequestDto request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        studentService.updateStudentById(request, userId);
        var updatedStudentDto = userService.getMeDtoById(userId);

        return ResponseEntity.ok(updatedStudentDto);
    }

    @PutMapping("/teachers/me")
    public ResponseEntity<Object> updateMeTeacher(@Valid @RequestBody UpdateTeacherRequestDto request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        teacherService.updateTeacherById(request, userId);
        var updatedTeacherDto = userService.getMeDtoById(userId);

        return ResponseEntity.ok(updatedTeacherDto);
    }


    @GetMapping("/week-hours")
    public ResponseEntity<Integer> getMyWeeklyHours(Principal principal) {
        AppUser user = userService.getCurrentUser(principal);
        int weeklyHours = userService.getWeeklyHour(user);
        return ResponseEntity.ok(weeklyHours);
    }

}
