package com.project.sales_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sales_api.Enums.Roles;
import com.project.sales_api.dto.UserRequestDTO;
import com.project.sales_api.dto.UserResponseDTO;
import com.project.sales_api.infra.security.SecurityFilter;
import com.project.sales_api.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserServiceImpl userService;
    @MockitoBean
    private SecurityFilter securityFilter;

    @Test
    void shouldCreateUser() throws Exception{
        UserRequestDTO requestDTO = new UserRequestDTO("João", "joao@joao.com", "joao123", Roles.ADMIN);
        UserResponseDTO responseDTO = new UserResponseDTO(1L,"João", "joao@joao.com", Roles.ADMIN);

        when(userService.createUser(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(responseDTO.name())));

        verify(userService).createUser(requestDTO);
    }

    @Test
    void shoulFindUserById() throws Exception{
        long userId = 1L;
        UserResponseDTO responseDTO = new UserResponseDTO(userId,"João", "joao@joao.com", Roles.ADMIN);

        when(userService.findUserById(userId)).thenReturn(responseDTO);

        mockMvc.perform(get("/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(responseDTO.name())))
                .andExpect(jsonPath("$.email", is(responseDTO.email())));

        verify(userService).findUserById(userId);
    }

    @Test
    void shouldFindAllUsers() throws Exception{
        List<UserResponseDTO> dtoResponseList = List.of(
                new UserResponseDTO(1L,"João", "joao@joao.com", Roles.ADMIN),
                new UserResponseDTO(2L,"Cleiton", "cleiton@cleiton.com", Roles.ADMIN)
        );

        when(userService.findAllUsers()).thenReturn(dtoResponseList);

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(dtoResponseList.size())));

        verify(userService).findAllUsers();
    }

    @Test
    void shouldUpdateUserById() throws Exception{
        long userId = 1L;
        UserRequestDTO dtoResquest = new UserRequestDTO("João Atualizado", "emailnovo@emailnovo.com", "senhanova", Roles.ADMIN);
        UserResponseDTO dtoResponse = new UserResponseDTO(userId, "João Atualizado", "emailnovo@emailnovo.com", Roles.ADMIN);

        when(userService.findUserById(userId)).thenReturn(dtoResponse);
        when(userService.updateUserById(userId, dtoResquest)).thenReturn(dtoResponse);

        mockMvc.perform(put("/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoResquest)))
                .andExpect(jsonPath("$.name", is(dtoResponse.name())))
                .andExpect(jsonPath("$.email", is(dtoResponse.email())));

        verify(userService).updateUserById(userId, dtoResquest);
    }

    @Test
    void shouldDeleteUserById() throws Exception{
        long userId = 1L;
        UserResponseDTO responseDTO = new UserResponseDTO(userId, "Cleiton", "cleiton@cleiton.com", Roles.ADMIN);

        when(userService.findUserById(userId)).thenReturn(responseDTO);
        doNothing().when(userService).deleteUserById(userId);

        mockMvc.perform(delete("/v1/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(userId);
    }
}
