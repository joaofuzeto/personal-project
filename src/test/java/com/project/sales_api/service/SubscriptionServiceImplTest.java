package com.project.sales_api.service;

import com.project.sales_api.Enums.SubscriptionStatus;
import com.project.sales_api.dto.SubscriptionRequestDTO;
import com.project.sales_api.dto.SubscriptionResponseDTO;
import com.project.sales_api.entity.Customer;
import com.project.sales_api.entity.Subscription;
import com.project.sales_api.exception.CustomerNotFoundException;
import com.project.sales_api.exception.SubscriptionNotFoundException;
import com.project.sales_api.repository.CustomerRepository;
import com.project.sales_api.repository.SubscriptionRepository;
import com.project.sales_api.service.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceImplTest {

    @Mock
    SubscriptionRepository subscriptionRepository;

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    SubscriptionServiceImpl subscriptionService;

    @Test
    void shouldCreateSubscriptionSuccessfully(){
        Long id = 1L;

        SubscriptionRequestDTO dtoRequest = new SubscriptionRequestDTO("Curso de Programação", BigDecimal.valueOf(99.90), id);

        Customer customer = new Customer();
        customer.setId(id);
        customer.setName("João");

        Subscription subscription = new Subscription();
        subscription.setPlanName("Curso de Programação");
        subscription.setPrice(BigDecimal.valueOf(99.90));
        subscription.setCustomer(customer);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        SubscriptionResponseDTO dtoResponse = subscriptionService.createSubscription(dtoRequest);

        assertEquals("Curso de Programação", dtoResponse.planName());
        assertEquals(BigDecimal.valueOf(99.90), dtoResponse.price());
        assertEquals("João", dtoResponse.customerName());

        verify(customerRepository, times(1)).findById(id);
        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    void shouldThrowExceptionWhenCustomerNoFound(){
        Long id = 1L;

        SubscriptionRequestDTO dtoRequest = new SubscriptionRequestDTO("Curso", BigDecimal.valueOf(99.90), id);

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {subscriptionService.createSubscription(dtoRequest);});

        verify(customerRepository).findById(id);
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void shouldReturnSubscriptionWhenIdExists(){
        Long id = 1L;

        Customer customer = new Customer();
        customer.setName("João");

        Subscription subscription = new Subscription();
        subscription.setId(id);
        subscription.setPlanName("Curso");
        subscription.setPrice(BigDecimal.valueOf(99.90));
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setCustomer(customer);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusMonths(2));

        when(subscriptionRepository.findById(id)).thenReturn(Optional.of(subscription));

        SubscriptionResponseDTO dtoResponse = subscriptionService.findSubscriptionById(id);

        assertNotNull(dtoResponse);
        assertEquals(id, dtoResponse.id());
        assertEquals("Curso", dtoResponse.planName());
        assertEquals(BigDecimal.valueOf(99.90), dtoResponse.price());
        assertEquals("João", dtoResponse.customerName());

        verify(subscriptionRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenSubscriptionNotFound(){
        Long id = 1L;

        when(subscriptionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class, () -> {subscriptionService.findSubscriptionById(id);});
        verify(subscriptionRepository).findById(id);
    }

    @Test
    void shouldReturnAllSubscriptions(){

        Customer customer = new Customer();
        customer.setName("João");

        Subscription subscription1 = new Subscription();
        subscription1.setId(1L);
        subscription1.setPlanName("Curso");
        subscription1.setPrice(BigDecimal.valueOf(99.90));
        subscription1.setStatus(SubscriptionStatus.ACTIVE);
        subscription1.setCustomer(customer);
        subscription1.setStartDate(LocalDate.now());
        subscription1.setEndDate(LocalDate.now().plusMonths(2));

        Subscription subscription2 = new Subscription();
        subscription2.setId(2L);
        subscription2.setPlanName("Curso2");
        subscription2.setPrice(BigDecimal.valueOf(105.90));
        subscription2.setStatus(SubscriptionStatus.ACTIVE);
        subscription2.setCustomer(customer);
        subscription2.setStartDate(LocalDate.now());
        subscription2.setEndDate(LocalDate.now().plusMonths(2));

        List<Subscription> subscriptionsList = List.of(subscription1, subscription2);

        when(subscriptionRepository.findAll()).thenReturn(subscriptionsList);

        List<SubscriptionResponseDTO> dtoList = subscriptionService.findAllSubscriptions();

        assertEquals(2, dtoList.size());
        assertEquals("Curso", dtoList.get(0).planName());
        assertEquals("Curso2", dtoList.get(1).planName());
        assertEquals("João", dtoList.get(0).customerName());
        assertEquals("João", dtoList.get(1).customerName());

        verify(subscriptionRepository).findAll();
    }

    void shouldReturnEmptyListWhenNoSubscriptionExist(){
        when(subscriptionRepository.findAll()).thenReturn(Collections.emptyList());

        List<SubscriptionResponseDTO> dtoList = subscriptionService.findAllSubscriptions();

        assertTrue(dtoList.isEmpty());
        verify(subscriptionRepository).findAll();
    }

    @Test
    void shouldUpdateSubscriptionSuccessfully(){
        Long id = 1L;

        SubscriptionRequestDTO dtoRequest = new SubscriptionRequestDTO("Novo Nome", BigDecimal.valueOf(150.00), id);

        Customer customer = new Customer();
        customer.setName("João");

        Subscription subscription1 = new Subscription();
        subscription1.setId(id);
        subscription1.setPlanName("Curso");
        subscription1.setPrice(BigDecimal.valueOf(99.90));
        subscription1.setStatus(SubscriptionStatus.ACTIVE);
        subscription1.setCustomer(customer);
        subscription1.setStartDate(LocalDate.now());
        subscription1.setEndDate(LocalDate.now().plusMonths(2));

        when(subscriptionRepository.findById(id)).thenReturn(Optional.of(subscription1));
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SubscriptionResponseDTO dtoResponse = subscriptionService.updateSubscriptionById(id, dtoRequest);

        assertEquals("Novo Nome", dtoResponse.planName());
        assertEquals(BigDecimal.valueOf(150.00), dtoResponse.price());

        verify(subscriptionRepository).findById(id);
        verify(subscriptionRepository).save(subscription1);

    }

    @Test
    void ShouldUpdateOnlyPlanNameWhenPriceIsNull(){
        Long id = 1L;

        SubscriptionRequestDTO dtoRequest = new SubscriptionRequestDTO("Novo Nome", null, id);

        Customer customer = new Customer();
        customer.setName("João");

        Subscription subscription1 = new Subscription();
        subscription1.setId(id);
        subscription1.setPlanName("Curso");
        subscription1.setPrice(BigDecimal.valueOf(99.90));
        subscription1.setStatus(SubscriptionStatus.ACTIVE);
        subscription1.setCustomer(customer);
        subscription1.setStartDate(LocalDate.now());
        subscription1.setEndDate(LocalDate.now().plusMonths(2));

        when(subscriptionRepository.findById(id)).thenReturn(Optional.of(subscription1));
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));

        SubscriptionResponseDTO dtoResponse = subscriptionService.updateSubscriptionById(id, dtoRequest);

        assertEquals("Novo Nome", dtoResponse.planName());
        assertEquals(BigDecimal.valueOf(99.90), dtoResponse.price());

        verify(subscriptionRepository).findById(id);
        verify(subscriptionRepository).save(subscription1);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingSubscription(){
        Long id = 1L;

        SubscriptionRequestDTO dtoRequest = new SubscriptionRequestDTO("Curso", BigDecimal.valueOf(100.00), id);

        when(subscriptionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class, () -> {subscriptionService.updateSubscriptionById(id, dtoRequest);});

        verify(subscriptionRepository).findById(id);
        verify(subscriptionRepository, never()).save(any());

    }

    @Test
    void shouldDeleteSubscriptionSuccessfully(){
        Long id = 1L;
        Subscription subscription = new Subscription();
        subscription.setId(id);

        when(subscriptionRepository.findById(id)).thenReturn(Optional.of(subscription));
        doNothing().when(subscriptionRepository).delete(subscription);

        subscriptionService.deleteSubscriptionById(id);

        verify(subscriptionRepository).findById(id);
        verify(subscriptionRepository).delete(subscription);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingSubscription(){
        Long id = 1L;

        when(subscriptionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class, () -> {subscriptionService.deleteSubscriptionById(id);});

        verify(subscriptionRepository).findById(id);
        verify(subscriptionRepository, never()).delete(any());
    }

}
