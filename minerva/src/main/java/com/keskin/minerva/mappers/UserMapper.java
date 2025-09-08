package com.keskin.minerva.mappers;

import com.keskin.minerva.dtos.users.UserDto;
import com.keskin.minerva.entities.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserDto entityToDto(AppUser user);
}
