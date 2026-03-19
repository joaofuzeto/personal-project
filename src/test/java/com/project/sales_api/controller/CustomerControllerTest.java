package com.project.sales_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sales_api.dto.CustomerRequestDTO;
import com.project.sales_api.dto.CustomerResponseDTO;
import com.project.sales_api.entity.Customer;
import com.project.sales_api.infra.security.SecurityFilter;
import com.project.sales_api.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private CustomerServiceImpl customerService;
    @MockitoBean
    private SecurityFilter securityFilter;

    @Test
    void shouldCreateCustomer() throws Exception{

        CustomerRequestDTO requestDTO = new CustomerRequestDTO("João", "joao@joao.com", "12345678910");
        CustomerResponseDTO responseDTO = new CustomerResponseDTO(1L, "João", "joao@joao.com", "12345678910");

        when(customerService.createCustomer(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("João"));

        verify(customerService).createCustomer(requestDTO);
    }

    @Test
    void shouldFindCustomerById() throws Exception{
        long customerId = 1L;
        CustomerResponseDTO responseDTO = new CustomerResponseDTO(customerId, "João", "joao@joao.com", "12345678910");

        when(customerService.findCustomerByID(customerId)).thenReturn(responseDTO);

        mockMvc.perform(get("/v1/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João"))
                .andExpect(jsonPath("$.email").value("joao@joao.com"))
                .andExpect(jsonPath("$.document").value("12345678910"));
    }

    @Test
    void shouldReturnAllCustomers() throws Exception{

        List<CustomerResponseDTO> listOfResponseDtos = List.of(
                new CustomerResponseDTO(1L,"João", "joao@joao.com", "12345678910"),
                new CustomerResponseDTO(2L, "Monique", "monique@monique.com", "12345678911")
        );

        when(customerService.findAllCustomers()).thenReturn(listOfResponseDtos);

        mockMvc.perform(get("/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listOfResponseDtos.size())));
    }

    @Test
    void shouldUpdateCustomerById() throws Exception{
        long customerId = 1L;
        CustomerRequestDTO requestDTO = new CustomerRequestDTO("João Atualizado", "joaonovo@joaonovo.com", "12345678910");
        CustomerResponseDTO responseDTO = new CustomerResponseDTO(customerId,"João Atualizado", "joaonovo@joaonovo.com", "12345678910");

        when(customerService.findCustomerByID(customerId)).thenReturn(responseDTO);
        when(customerService.updateCustomer(customerId, requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(put("/v1/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(responseDTO.name())))
                .andExpect(jsonPath("$.email", is(responseDTO.email())))
                .andExpect(jsonPath("$.document", is(responseDTO.document())));
    }

    @Test
    void shouldDeleteCustomerById() throws Exception{
        long customerId = 1L;

        CustomerResponseDTO responseDTO = new CustomerResponseDTO(customerId,"João", "joao@joao.com", "12345678910");

        when(customerService.findCustomerByID(customerId)).thenReturn(responseDTO);
        doNothing().when(customerService).deleteCustomerById(customerId);

        mockMvc.perform(delete("/v1/customers/{id}", customerId))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerById(customerId);
    }
}
