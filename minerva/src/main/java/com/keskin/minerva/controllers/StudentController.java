package com.keskin.minerva.controllers;

import com.keskin.minerva.dtos.students.StudentDto;
import com.keskin.minerva.dtos.students.UpdateStudentRequestDto;
import com.keskin.minerva.entities.TeacherNote;
import com.keskin.minerva.services.IStudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/students", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
@AllArgsConstructor
@Validated
public class StudentController {

    private final IStudentService studentService;

    @GetMapping("/getAll")
    public ResponseEntity<List<StudentDto>> getAllStudents(){
        var students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudent(@PathVariable Long id){
        var student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/getAllActive")
    public ResponseEntity<List<StudentDto>> getAllActiveStudents(){
        var students = studentService.getAllActiveStudents();
        return ResponseEntity.ok(students);
    }

    @Operation(
            summary = "Assign a lesson to a student",
            description = "Assigns the specified lesson to the student identified by studentId and returns the updated student data.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Lesson assigned successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StudentDto.class))),
                    @ApiResponse(responseCode = "404", description = "Student or lesson not found")
            }
    )
    @PostMapping("/{studentId}/lessons/{lessonId}")
    public ResponseEntity<StudentDto> addStudentToClassroom(
            @Parameter(description = "ID of the student", required = true)
            @PathVariable Long studentId,
            @Parameter(description = "ID of the lesson", required = true)
            @PathVariable Long lessonId){
        var student = studentService.assignLesson(studentId, lessonId);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @Operation(
            summary = "Update student information",
            description = "Updates the details of the student identified by id using the provided request data.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Student updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StudentDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Student not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(
            @Parameter(description = "ID of the student to update", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateStudentRequestDto request){
        var updatedStudent = studentService.updateStudentById(request, id);
        return ResponseEntity.ok(updatedStudent);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StudentDto> freezeStudent(@PathVariable Long id){
        var frozenStudent = studentService.freezeStudent(id);
        return ResponseEntity.ok(frozenStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/notes")
    public ResponseEntity<Void> addTeacherNote(@PathVariable Long id, @RequestBody TeacherNote note){
        studentService.addTeacherNote(id, note);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{studentId}/notes/{teacherId}")
    public ResponseEntity<Void> deleteTeacherNote(@PathVariable Long studentId, @PathVariable Long teacherId){
        studentService.deleteTeacherNote(studentId, teacherId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all teacher notes for a student",
            description = "Returns a map of teacher IDs and their notes for the student identified by id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Teacher notes retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TeacherNote.class))),
                    @ApiResponse(responseCode = "404", description = "Student not found")
            }
    )
    @GetMapping("/{id}/notes")
    public ResponseEntity<Map<Long, TeacherNote>> getTeacherNotes(
            @Parameter(description = "ID of the student", required = true)
            @PathVariable Long id){
        var student = studentService.getAllTeacherNotes(id);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{studentId}/lessons/{lessonId}")
    public ResponseEntity<Void> removeStudentFromClassroom(@PathVariable Long studentId, @PathVariable Long lessonId){
        studentService.removeLesson(studentId, lessonId);
        return ResponseEntity.noContent().build();
    }

}
