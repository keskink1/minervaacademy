package com.keskin.minerva.mappers;

import com.keskin.minerva.dtos.students.CreateStudentRequestDto;
import com.keskin.minerva.dtos.students.StudentDto;
import com.keskin.minerva.dtos.students.UpdateStudentRequestDto;
import com.keskin.minerva.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StudentMapper {

    StudentDto entityToDto(Student student);

    Student updateDtoToEntity(UpdateStudentRequestDto requestDto, @MappingTarget Student student);

    Student createRequestToEntity(CreateStudentRequestDto requestDto);
}
