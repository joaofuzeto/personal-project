package com.project.sales_api.service.impl;

import com.project.sales_api.dto.UserRequestDTO;
import com.project.sales_api.dto.UserResponseDTO;
import com.project.sales_api.entity.Users;
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
        return new UserResponseDTO(userSaved.getName(), userSaved.getEmail(),
                userSaved.getRole());
    }

    @Override
    public UserResponseDTO findUserById(Long id) {

        var userFound = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new UserResponseDTO(userFound.getName(), userFound.getEmail(), userFound.getRole());
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(
                        user.getName(),
                        user.getEmail(),
                        user.getRole()))
                .toList();
    }

    @Override
    public UserResponseDTO updateUserById(Long id, UserRequestDTO userRequestDTO) {
        var userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não existe"));

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

        return new UserResponseDTO(userEntity.getName(), userEntity.getEmail(), userEntity.getRole());
    }

    @Override
    public void deleteUserById(Long id) {
        var userExist = userRepository.existsById(id);

        if(userExist){
            userRepository.deleteById(id);
        } else{
            throw new RuntimeException("Usuário não existe");
        }
    }
}
