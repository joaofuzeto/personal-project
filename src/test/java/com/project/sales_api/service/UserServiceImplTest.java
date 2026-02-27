package com.project.sales_api.service;

import com.project.sales_api.Enums.Roles;
import com.project.sales_api.dto.UserRequestDTO;
import com.project.sales_api.dto.UserResponseDTO;
import com.project.sales_api.entity.Users;
import com.project.sales_api.exception.UserNotFoundException;
import com.project.sales_api.repository.UserRepository;
import com.project.sales_api.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    void shouldCreateUserSuccessfully(){
        Users user = new Users();
        user.setId(1L);
        user.setName("João");
        user.setEmail("joao@joao.com");
        user.setPassword("joao123");
        user.setRole(Roles.ADMIN);
        user.setCreatedAt(LocalDateTime.now());

        UserRequestDTO dtoRequest = new UserRequestDTO("João", "joao@joao.com", "joao123", Roles.ADMIN);

        when(userRepository.save(any(Users.class))).thenReturn(user);

        UserResponseDTO dtoResponse = userService.createUser(dtoRequest);

        assertEquals("João", dtoResponse.name());
        assertEquals("joao@joao.com", dtoResponse.email());
        assertEquals(Roles.ADMIN, dtoResponse.role());

        verify(userRepository).save(any(Users.class));
    }

    @Test
    void shouldReturnUserById(){
        Long id = 1L;

        Users user = new Users();
        user.setId(id);
        user.setName("João");
        user.setEmail("joao@joao.com");
        user.setPassword("joao123");
        user.setRole(Roles.USER);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserResponseDTO dtoResponse = userService.findUserById(id);

        assertEquals("João", dtoResponse.name());
        assertEquals("joao@joao.com", dtoResponse.email());
        assertEquals(Roles.USER, dtoResponse.role());

        verify(userRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound(){
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {userService.findUserById(id);});

        verify(userRepository).findById(id);
    }

    @Test
    void shouldReturnAllUsers(){
        Users user1 = new Users();
        user1.setId(1L);
        user1.setName("João Victor");
        user1.setEmail("joaov@joaov.com");
        user1.setPassword("joao123");
        user1.setRole(Roles.ADMIN);
        user1.setCreatedAt(LocalDateTime.now());

        Users user2 = new Users();
        user2.setId(1L);
        user2.setName("Cleber Pereira");
        user2.setEmail("cleberp@cleberp.com");
        user2.setPassword("cleber456");
        user2.setRole(Roles.ADMIN);
        user2.setCreatedAt(LocalDateTime.now());

        List<Users> usersList = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(usersList);

        List<UserResponseDTO> dtoList = userService.findAllUsers();

        assertEquals(2, dtoList.size());
        assertEquals("João Victor", dtoList.get(0).name());
        assertEquals("Cleber Pereira", dtoList.get(1).name());
        assertEquals("joaov@joaov.com", dtoList.get(0).email());
        assertEquals("cleberp@cleberp.com", dtoList.get(1).email());

        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUserExist(){
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserResponseDTO> dtoList = userService.findAllUsers();

        assertTrue(dtoList.isEmpty());

        verify(userRepository).findAll();
    }

    @Test
    void shouldUpdateUserSuccessfully(){
        Long id = 1L;
        Users user1 = new Users();
        user1.setId(id);
        user1.setName("João Victor");
        user1.setEmail("joaov@joaov.com");
        user1.setPassword("joao123");
        user1.setRole(Roles.ADMIN);
        user1.setCreatedAt(LocalDateTime.now());

        UserRequestDTO dtoRequest = new UserRequestDTO("Novo Nome", null, null, null);

        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(Users.class))).thenAnswer(inv -> inv.getArgument(0));

        UserResponseDTO dtoResponse = userService.updateUserById(id, dtoRequest);

        assertEquals("Novo Nome", dtoResponse.name());
        assertEquals("joaov@joaov.com", dtoResponse.email());
        assertEquals(Roles.ADMIN, dtoResponse.role());

        verify(userRepository).findById(id);
        verify(userRepository).save(any(Users.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingUser(){
        Long id = 1L;

        UserRequestDTO dtoRequest = new UserRequestDTO("João", "joao@joao.com", "joao123", Roles.ADMIN);

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {userService.updateUserById(id, dtoRequest);});

        verify(userRepository).findById(id);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldDeleteUserSuccessfully(){
        Long id = 1L;

        Users user = new Users();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUserById(id);

        verify(userRepository).findById(id);
        verify(userRepository).delete(user);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser(){
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {userService.deleteUserById(id);});

        verify(userRepository).findById(id);
        verify(userRepository, never()).delete(any());
    }

}
