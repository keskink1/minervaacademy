package com.keskin.minerva.controllers;

import com.keskin.minerva.dtos.lessons.LessonDto;
import com.keskin.minerva.dtos.lessons.CreateLessonRequestDto;
import com.keskin.minerva.services.ILessonService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/lessons", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
@AllArgsConstructor
@Validated
public class LessonController {

    private final ILessonService lessonService;

    @GetMapping("/getAll")
    public ResponseEntity<List<LessonDto>> getAllLessons(){
        var lessons = lessonService.getAllLessons();
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getLesson(@PathVariable Long id){
        var lesson = lessonService.getLessonById(id);
        return ResponseEntity.ok(lesson);
    }

    @Operation(
            summary = "Create a new lesson",
            description = "Creates a new lesson using the provided request data and returns the created lesson with its details.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Lesson created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LessonDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PostMapping("/create")
    public ResponseEntity<LessonDto> createLesson(
            @RequestBody CreateLessonRequestDto request,
            UriComponentsBuilder builder){
        var lesson = lessonService.createLesson(request);

        var location = builder
                .path("/api/users/{id}")
                .buildAndExpand(lesson.getLectureCode())
                .toUri();

        return ResponseEntity.created(location).body(lesson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id){
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get lessons of a specific student",
            description = "Returns a set of lessons assigned to the student identified by studentId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lessons retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LessonDto.class))),
                    @ApiResponse(responseCode = "404", description = "Student not found")
            }
    )
    @GetMapping("/{studentId}/lessons")
    @PreAuthorize("hasRole('ADMIN') or #studentId == principal")
    public ResponseEntity<Set<LessonDto>> getLessonsOfStudent(
            @Parameter(description = "ID of the student", required = true)
            @PathVariable Long studentId) {
        var lessons = lessonService.getLessonsOfStudent(studentId);
        return ResponseEntity.ok(lessons);
    }


    @Operation(
            summary = "Get lessons of a specific teacher",
            description = "Returns a set of lessons assigned to the teacher identified by teacherId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lessons retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LessonDto.class))),
                    @ApiResponse(responseCode = "404", description = "Teacher not found")
            }
    )
    @GetMapping("/{teacherId}/lessons")
    @PreAuthorize("hasRole('ADMIN') or #teacherId == principal")
    public ResponseEntity<Set<LessonDto>> getLessonsOfTeacher(
            @Parameter(description = "ID of the teacher", required = true)
            @PathVariable Long teacherId) {
        var lessons = lessonService.getLessonsOfTeacher(teacherId);
        return ResponseEntity.ok(lessons);
    }
}
