package com.project.sales_api.controller;

import com.project.sales_api.dto.UserRequestDTO;
import com.project.sales_api.dto.UserResponseDTO;
import com.project.sales_api.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO){
        userServiceImpl.createUser(userRequestDTO);
        String message = "O usuário " + userRequestDTO.name() + " foi criado com sucesso";

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable("userId") Long id){
        UserResponseDTO userFound = userServiceImpl.findUserById(id);

        return ResponseEntity.ok(userFound);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listAllUsers(){
        var users = userServiceImpl.findAllUsers();

        return ResponseEntity.ok(users);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> updateUserById(@PathVariable("userId") Long id, @Valid @RequestBody UserRequestDTO userRequestDTO){
        var userUpdated = userServiceImpl.updateUserById(id, userRequestDTO);

        return ResponseEntity.ok(userUpdated);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("userId") Long id){
        userServiceImpl.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }
}
