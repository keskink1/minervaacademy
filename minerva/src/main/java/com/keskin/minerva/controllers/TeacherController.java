package com.keskin.minerva.controllers;

import com.keskin.minerva.dtos.teachers.TeacherDto;
import com.keskin.minerva.dtos.teachers.TeacherLessonDto;
import com.keskin.minerva.dtos.teachers.UpdateTeacherRequestDto;
import com.keskin.minerva.services.ITeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/teachers", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
@AllArgsConstructor
@Validated
public class TeacherController {

    private final ITeacherService teacherService;

    @GetMapping("getAll")
    public ResponseEntity<List<TeacherDto>> getAllTeachers() {
        var teacher = teacherService.getAllTeachers();
        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/getAllActive")
    public ResponseEntity<List<TeacherDto>> getAllActiveTeachers() {
        var teacher = teacherService.getAllActiveTeachers();
        return ResponseEntity.ok(teacher);
    }

    @Operation(
            summary = "Get all lessons of a specific teacher",
            description = "Returns a set of lessons assigned to the teacher identified by teacherId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of lessons returned successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TeacherLessonDto.class))),
                    @ApiResponse(responseCode = "404", description = "Teacher not found")
            }
    )
    @GetMapping("/{teacherId}/lessons")
    public ResponseEntity<Set<TeacherLessonDto>> getLessonsOfTeacher(
            @Parameter(description = "ID of the teacher", required = true)
            @PathVariable Long teacherId) {
        var teacher = teacherService.getTeacherLessonsById(teacherId);
        return ResponseEntity.ok(teacher);
    }

    @Operation(
            summary = "Assign a lesson to a teacher",
            description = "Assigns the specified lesson to the teacher identified by teacherId and returns the updated teacher data.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lesson assigned successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TeacherDto.class))),
                    @ApiResponse(responseCode = "404", description = "Teacher or lesson not found")
            }
    )
    @PostMapping("/{teacherId}/lessons/{lessonId}")
    public ResponseEntity<TeacherDto> addTeacherToClassroom(
            @Parameter(description = "ID of the teacher", required = true)
            @PathVariable Long teacherId,
            @Parameter(description = "ID of the lesson", required = true)
            @PathVariable Long lessonId) {
        var teacher = teacherService.assignLesson(teacherId, lessonId);
        return ResponseEntity.ok(teacher);
    }

    @Operation(
            summary = "Update teacher information",
            description = "Updates the details of the teacher identified by id using the provided request data.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Teacher updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TeacherDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Teacher not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<TeacherDto> updateTeacher(
            @Parameter(description = "ID of the teacher to update", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateTeacherRequestDto request) {
        var teacher = teacherService.updateTeacherById(request, id);
        return ResponseEntity.ok(teacher);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TeacherDto> freezeTeacher(@PathVariable Long id) {
        var teacher = teacherService.freezeTeacher(id);
        return ResponseEntity.ok().body(teacher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{teacherId}/lessons/{lessonId}")
    public ResponseEntity<Void> removeLesson(@PathVariable Long teacherId, @PathVariable Long lessonId) {
        teacherService.removeLesson(teacherId, lessonId);
        return ResponseEntity.noContent().build();
    }


}
