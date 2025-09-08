package com.keskin.minerva.config;

import com.keskin.minerva.entities.*;
import com.keskin.minerva.repositories.LessonRepository;
import com.keskin.minerva.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
@Transactional
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Admin
        if (userRepository.findByEmail("admin@minerva.com").isEmpty()) {
            AppUser admin = new AppUser();
            admin.setEmail("admin@minerva.com");
            admin.setPassword(passwordEncoder.encode("Admin123!"));
            admin.setFirstName("System");
            admin.setLastName("Admin");
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);
        }

        // Teacher
        if (userRepository.findByEmail("teacher@minerva.com").isEmpty()) {
            Teacher teacher = new Teacher();
            teacher.setEmail("teacher@minerva.com");
            teacher.setPassword(passwordEncoder.encode("Teacher123!"));
            teacher.setFirstName("Paula");
            teacher.setLastName("Delmarín");
            teacher.setRole(Role.TEACHER);
            teacher.setEnabled(true);
            teacher.setPhoneNumber("+345551112233");
            userRepository.save(teacher);
        }

        // Student
        if (userRepository.findByEmail("student@minerva.com").isEmpty()) {
            Student student = new Student();
            student.setEmail("student@minerva.com");
            student.setPassword(passwordEncoder.encode("Student123!"));
            student.setFirstName("Sara");
            student.setLastName("Garcia");
            student.setRole(Role.STUDENT);
            student.setEnabled(true);
            student.setEnrollmentNumber("S1001");
            student.setDateOfBirth(LocalDate.of(2005, 5, 15));
            userRepository.save(student);
        }


    }
}
