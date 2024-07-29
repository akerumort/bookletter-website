package com.akerumort.userservice.mappers;

import com.akerumort.userservice.dto.UserCreateDTO;
import com.akerumort.userservice.dto.UserDTO;
import com.akerumort.userservice.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserCreateDTO userCreateDTO);

    UserDTO toDTO(User user);
}
