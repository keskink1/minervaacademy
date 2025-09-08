package com.keskin.minerva.services.impl;

import com.keskin.minerva.dtos.students.CreateStudentRequestDto;
import com.keskin.minerva.dtos.students.StudentDto;
import com.keskin.minerva.dtos.teachers.CreateTeacherRequestDto;
import com.keskin.minerva.dtos.teachers.TeacherDto;
import com.keskin.minerva.dtos.users.ChangePasswordRequestDto;
import com.keskin.minerva.entities.AppUser;
import com.keskin.minerva.entities.Student;
import com.keskin.minerva.entities.Teacher;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.security.core.userdetails.User;
import com.keskin.minerva.exceptions.ResourceAlreadyExistsException;
import com.keskin.minerva.exceptions.ResourceNotFoundException;
import com.keskin.minerva.mappers.StudentMapper;
import com.keskin.minerva.mappers.TeacherMapper;
import com.keskin.minerva.mappers.UserMapper;
import com.keskin.minerva.repositories.StudentRepository;
import com.keskin.minerva.repositories.TeacherRepository;
import com.keskin.minerva.repositories.UserRepository;
import com.keskin.minerva.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService, UserDetailsService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final StudentMapper studentMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser getCurrentUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", principal.getName()));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found!"));

        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public StudentDto createStudent(CreateStudentRequestDto request) {
        if (studentRepository.findByEmail(request.getEmail()).isPresent() ) {
            throw new ResourceAlreadyExistsException("Email", request.getEmail());
        }

        if (studentRepository.findByEnrollmentNumber(request.getEnrollmentNumber())
        ) {
            throw new ResourceAlreadyExistsException("Enrollment number", request.getEnrollmentNumber());
        }

        var student = studentMapper.createRequestToEntity(request);

        studentRepository.save(student);
        return studentMapper.entityToDto(student);
    }

    @Override
    public TeacherDto createTeacher(CreateTeacherRequestDto request) {
        if (teacherRepository.findByEmail(request.getEmail()).isPresent() ) {
            throw new ResourceAlreadyExistsException("Email", request.getEmail());
        }

        if (teacherRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()
        ) {
            throw new ResourceAlreadyExistsException("Phone number", request.getPhoneNumber());
        }

        var teacher = teacherMapper.createRequestToEntity(request);

        teacherRepository.save(teacher);
        return teacherMapper.entityToDto(teacher);
    }

    public Object getMeDtoById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser", "ID", id.toString()));

        if (user instanceof Student student) {
            return studentMapper.entityToDto(student);
        } else if (user instanceof Teacher teacher) {
            return teacherMapper.entityToDto(teacher);
        }

        return userMapper.entityToDto(user);
    }

    @Override
    public Optional<AppUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public int getWeeklyHour(AppUser user) {
        if (user instanceof Student student){
            return student.calculateWeeklyHours();
        }
        else if (user instanceof Teacher teacher){
            return teacher.calculateWeeklyHours();
        }
        return 0;
    }

    @Override
    public void changePassword(String username, ChangePasswordRequestDto request) {
        AppUser user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
