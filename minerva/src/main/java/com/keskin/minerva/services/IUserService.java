package com.keskin.minerva.services;

import com.keskin.minerva.dtos.students.CreateStudentRequestDto;
import com.keskin.minerva.dtos.students.StudentDto;
import com.keskin.minerva.dtos.teachers.CreateTeacherRequestDto;
import com.keskin.minerva.dtos.teachers.TeacherDto;
import com.keskin.minerva.dtos.users.ChangePasswordRequestDto;
import com.keskin.minerva.entities.AppUser;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Optional;

public interface IUserService {

    AppUser getCurrentUser(Principal principal);

    Optional<AppUser> findByEmail(String email);

    Object getMeDtoById(Long id);

    UserDetails loadUserByUsername(String email);

    StudentDto createStudent(CreateStudentRequestDto request);

    TeacherDto createTeacher(CreateTeacherRequestDto request);

    Optional<AppUser> getUserById(Long id);

    int getWeeklyHour(AppUser user);

    void changePassword(String username, ChangePasswordRequestDto request);

}
