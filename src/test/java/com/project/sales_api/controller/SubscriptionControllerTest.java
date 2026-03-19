package com.project.sales_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sales_api.Enums.SubscriptionStatus;
import com.project.sales_api.dto.SubscriptionRequestDTO;
import com.project.sales_api.dto.SubscriptionResponseDTO;
import com.project.sales_api.infra.security.SecurityFilter;
import com.project.sales_api.infra.security.TokenService;
import com.project.sales_api.service.impl.CustomerServiceImpl;
import com.project.sales_api.service.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SubscriptionControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private SubscriptionServiceImpl subscriptionService;
    @MockitoBean
    private SecurityFilter securityFilter;

    @Test
    void shouldCreateSubscription() throws Exception{

        SubscriptionRequestDTO requestDTO = new SubscriptionRequestDTO("Curso de Programação", BigDecimal.valueOf(250.00), 1L);
        SubscriptionResponseDTO responseDTO = new SubscriptionResponseDTO(1L , "Curso de Programação", BigDecimal.valueOf(250.00), SubscriptionStatus.ACTIVE, "João", LocalDate.now(), LocalDate.now().plusMonths(2));

        when(subscriptionService.createSubscription(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(post("/v1/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.planName", is(responseDTO.planName())));

        verify(subscriptionService).createSubscription(requestDTO);
    }

    @Test
    void shouldFindSubscriptionById() throws Exception{
        long subscriptionId = 1L;
        SubscriptionResponseDTO responseDTO = new SubscriptionResponseDTO(subscriptionId , "Curso de Programação", BigDecimal.valueOf(250.00), SubscriptionStatus.ACTIVE, "João", LocalDate.now(), LocalDate.now().plusMonths(2));

        when(subscriptionService.findSubscriptionById(subscriptionId)).thenReturn(responseDTO);

        mockMvc.perform(get("/v1/subscriptions/{id}", subscriptionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planName", is(responseDTO.planName())))
                .andExpect(jsonPath("$.customerName", is(responseDTO.customerName())));

        verify(subscriptionService).findSubscriptionById(subscriptionId);
    }

    @Test
    void shouldFindAllSubscriptions() throws Exception{
        List<SubscriptionResponseDTO> dtoResponseList = List.of(
                new SubscriptionResponseDTO(1L , "Curso de Programação", BigDecimal.valueOf(250.00), SubscriptionStatus.ACTIVE, "João", LocalDate.now(), LocalDate.now().plusMonths(2)),
                new SubscriptionResponseDTO(2L , "Curso de Culinária", BigDecimal.valueOf(300.50), SubscriptionStatus.ACTIVE, "Jorge", LocalDate.now(), LocalDate.now().plusMonths(2))
        );

        when(subscriptionService.findAllSubscriptions()).thenReturn(dtoResponseList);

        mockMvc.perform(get("/v1/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(dtoResponseList.size())));

        verify(subscriptionService).findAllSubscriptions();
    }

    @Test
    void shouldUpdateSubscription() throws Exception{
        long subscriptionId = 1L;
        SubscriptionRequestDTO requestDTO = new SubscriptionRequestDTO("Curso Novo", BigDecimal.valueOf(300.00), 1L);
        SubscriptionResponseDTO responseDTO = new SubscriptionResponseDTO(2L, "Curso Novo", BigDecimal.valueOf(300.00), SubscriptionStatus.ACTIVE, "João", LocalDate.now(), LocalDate.now().plusMonths(2));

        when(subscriptionService.findSubscriptionById(subscriptionId)).thenReturn(responseDTO);
        when(subscriptionService.updateSubscriptionById(subscriptionId, requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(put("/v1/subscriptions/{id}", subscriptionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planName", is(responseDTO.planName())))
                .andExpect(jsonPath("$.price", is(300.00)));

        verify(subscriptionService).updateSubscriptionById(subscriptionId, requestDTO);
    }

    @Test
    void shouldDeleteSubscriptionById() throws Exception{
        long subscriptionId = 1L;
        SubscriptionResponseDTO responseDTO = new SubscriptionResponseDTO(1L, "Curso Novo", BigDecimal.valueOf(300.00), SubscriptionStatus.ACTIVE, "João", LocalDate.now(), LocalDate.now().plusMonths(2));

        when(subscriptionService.findSubscriptionById(subscriptionId)).thenReturn(responseDTO);
        doNothing().when(subscriptionService).deleteSubscriptionById(subscriptionId);

        mockMvc.perform(delete("/v1/subscriptions/{id}", subscriptionId))
                .andExpect(status().isNoContent());

        verify(subscriptionService).deleteSubscriptionById(subscriptionId);
    }
}
