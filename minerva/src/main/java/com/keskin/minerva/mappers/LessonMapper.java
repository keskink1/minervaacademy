package com.keskin.minerva.mappers;

import com.keskin.minerva.dtos.lessons.LessonDto;
import com.keskin.minerva.dtos.lessons.CreateLessonRequestDto;
import com.keskin.minerva.entities.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LessonMapper {
    LessonDto entityToDto(Lesson lesson);

    Lesson createRequestToEntity(CreateLessonRequestDto requestDto);
}
