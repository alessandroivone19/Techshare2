package com.generation.techshare.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.generation.techshare.dto.UserDto;
import com.generation.techshare.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // User Entity -> UserDTO (omette password in output)
    @Mapping(target = "password", ignore = true)
    UserDto toDTO(User user);

    // UserDTO -> User Entity (accetta password in input)
    User toEntity(UserDto userDto);

   List<UserDto> toDtos (List<User> users);

   List<User> toEntity (List<UserDto> dtos);

}