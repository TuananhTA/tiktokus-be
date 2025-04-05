package com.tiktokus.tiktokus.Convert;

import com.tiktokus.tiktokus.DTO.UserDTO;
import com.tiktokus.tiktokus.Entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserConvert {

    public static UserDTO convertDTO(User user){
        return UserDTO
                .builder()
                .id(user.getId())
                .role(user.getRole())
                .phone(user.getPhone())
                .status(user.getStatus())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .balance(user.getBalance())
                .build();
    }
    public static User convertEntity(UserDTO userDTO){
        return User
                .builder()
                .role(userDTO.getRole())
                .phone(userDTO.getPhone())
                .status(userDTO.getStatus())
                .email(userDTO.getEmail())
                .fullName(userDTO.getFullName())
                .balance(userDTO.getBalance())
                .build();
    }

    public static List<UserDTO> convertListToDTO(List<User> users){
        return users.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getFullName(),
                        user.getPhone(),
                        user.getRole(),
                        user.getStatus(),
                        user.getBalance()))
                .collect(Collectors.toList());
    }

    public static List<User> convertListToEntity(List<UserDTO> userDTOs) {
        return userDTOs.stream()
                .map(userDTO -> User
                        .builder()
                        .role(userDTO.getRole())
                        .phone(userDTO.getPhone())
                        .status(userDTO.getStatus())
                        .email(userDTO.getEmail())
                        .fullName(userDTO.getFullName())
                        .balance(userDTO.getBalance())
                        .build())
                .collect(Collectors.toList());
    }
}
