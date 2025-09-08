package com.keskin.minerva.mappers;

import com.keskin.minerva.dtos.teachers.CreateTeacherRequestDto;
import com.keskin.minerva.dtos.teachers.TeacherDto;
import com.keskin.minerva.dtos.teachers.TeacherLessonDto;
import com.keskin.minerva.dtos.teachers.UpdateTeacherRequestDto;
import com.keskin.minerva.entities.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TeacherMapper {

    TeacherDto entityToDto(Teacher teacher);

    Teacher updateDtoToEntity(UpdateTeacherRequestDto requestDto, @MappingTarget Teacher teacher);

    TeacherLessonDto entityToTeacherLessonDto(Teacher teacher);

    Teacher createRequestToEntity(CreateTeacherRequestDto request);
}
