package com.project.sales_api.service;

import com.project.sales_api.dto.UserRequestDTO;
import com.project.sales_api.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO findUserById(Long id);
    List<UserResponseDTO> findAllUsers();
    UserResponseDTO updateUserById(Long id, UserRequestDTO userRequestDTO);
    void deleteUserById(Long id);

}
