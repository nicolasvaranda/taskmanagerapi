package com.taskmanager.mapper;

import com.taskmanager.model.dto.UserDTO;
import com.taskmanager.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }

    public UserDTO toDTO(User userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(userEntity.getName());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setId(userEntity.getId());
        userDTO.setCreatedAt(userEntity.getCreatedAt());
        return userDTO;
    }

    //updateUser
}
