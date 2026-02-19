package com.project.sales_api.service.impl;

import com.project.sales_api.dto.UserRequestDTO;
import com.project.sales_api.dto.UserResponseDTO;
import com.project.sales_api.entity.Users;
import com.project.sales_api.exception.UserNotFoundException;
import com.project.sales_api.repository.UserRepository;
import com.project.sales_api.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        var user = new Users();
        user.setName(userRequestDTO.name());
        user.setEmail(userRequestDTO.email());
        user.setPassword(userRequestDTO.password());
        user.setRole(userRequestDTO.role());
        user.setCreatedAt(LocalDateTime.now());

        var userSaved = userRepository.save(user);
        return toDto(userSaved);
    }

    @Override
    public UserResponseDTO findUserById(Long id) {

        var userFound = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        return toDto(userFound);
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserResponseDTO updateUserById(Long id, UserRequestDTO userRequestDTO) {
        var userEntity = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if(userRequestDTO.name() != null){
            userEntity.setName(userRequestDTO.name());
        }
        if(userRequestDTO.email() != null){
            userEntity.setEmail(userRequestDTO.email());
        }
        if(userRequestDTO.password() != null){
            userEntity.setPassword(userRequestDTO.password());
        }
        if(userRequestDTO.role() != null){
            userEntity.setRole(userRequestDTO.role());
        }

        userRepository.save(userEntity);

        return toDto(userEntity);
    }

    @Override
    public void deleteUserById(Long id) {
        var userExist = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        userRepository.delete(userExist);
    }

    private UserResponseDTO toDto(Users u){
        return new UserResponseDTO(
                u.getName(),
                u.getEmail(),
                u.getRole()
        );
    }
}
