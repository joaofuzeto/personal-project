package com.project.sales_api.service;

import com.project.sales_api.dto.CustomerRequestDTO;
import com.project.sales_api.dto.CustomerResponseDTO;
import com.project.sales_api.entity.Customer;
import com.project.sales_api.exception.CustomerNotFoundException;
import com.project.sales_api.repository.CustomerRepository;
import com.project.sales_api.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    CustomerRepository customerRepository;
    @InjectMocks
    CustomerServiceImpl customerService;

    @Test
    void shouldCreateCustomerSuccessfully(){

        //ARRANGE
        CustomerRequestDTO dtoRequest = new CustomerRequestDTO("João Paulo", "joaop@joaop.com", "12345678912");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("João Paulo");
        customer.setEmail("joaop@joaop.com");
        customer.setDocument("12345678912");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        //ACT
        CustomerResponseDTO dtoResponse = customerService.createCustomer(dtoRequest);

        //ASSERT
        assertEquals("João Paulo", dtoResponse.name());
        assertEquals("joaop@joaop.com", dtoResponse.email());
        assertEquals("12345678912", dtoResponse.document());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void shouldReturnCustomerWhenIdExists(){
        Long id = 1L;

        Customer customer = new Customer();
        customer.setId(id);
        customer.setName("Maria Albertina");
        customer.setEmail("maria@maria.com");
        customer.setDocument("78945612378");

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        CustomerResponseDTO dtoResponse = customerService.findCustomerByID(id);

        assertEquals("Maria Albertina", dtoResponse.name());
        assertEquals("maria@maria.com", dtoResponse.email());
        assertEquals("78945612378", dtoResponse.document());

        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound(){
        Long id = 1L;

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.findCustomerByID(id));

        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    void shouldReturnAllCustomers(){
        Customer customer1 = new Customer();
        customer1.setName("Maria Albertina");
        customer1.setEmail("maria@maria.com");
        customer1.setDocument("78945612378");

        Customer customer2 = new Customer();
        customer2.setName("Paulo Roberto");
        customer2.setEmail("roberto@roberto.com");
        customer2.setDocument("78945612322");

        List<Customer> customers = List.of(customer1, customer2);

        when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerResponseDTO> dtosList = customerService.findAllCustomers();

        assertEquals(2, dtosList.size());

        assertEquals("Maria Albertina", dtosList.get(0).name());
        assertEquals("Paulo Roberto", dtosList.get(1).name());

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoCustomers(){
        when(customerRepository.findAll()).thenReturn(List.of());

        List<CustomerResponseDTO> dtosList = customerService.findAllCustomers();

        assertTrue(dtosList.isEmpty());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void shouldUpdateCustomerSuccessfully(){
        Long id = 1L;

        Customer customerExisting = new Customer();
        customerExisting.setId(id);
        customerExisting.setName("Maria Albertina");
        customerExisting.setEmail("maria@maria.com");
        customerExisting.setDocument("78945612378");

        CustomerRequestDTO dtoRequest = new CustomerRequestDTO("Novo Nome", "novoemail@novoemail.com", "12345678912");

        when(customerRepository.findById(id)).thenReturn(Optional.of(customerExisting));

        CustomerResponseDTO dtoResponse = customerService.updateCustomer(id, dtoRequest);

        assertEquals("Novo Nome", dtoResponse.name());
        assertEquals("novoemail@novoemail.com", dtoResponse.email());
        assertEquals("12345678912", dtoResponse.document());

        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, times(1)).save(customerExisting);
    }

    @Test
    void shouldThrowExceptionWhenUpdateNonExistingCustomer(){
        Long id = 1L;

        CustomerRequestDTO dtoRequest = new CustomerRequestDTO("Novo nome", "novoemail@novoemail.com", "12345678912");

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {customerService.updateCustomer(id, dtoRequest);});

        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, never()).save(any());
    }

    @Test
    void shouldDeleteCustomerSuccessfully(){
        Long id = 1L;

        Customer customer = new Customer();
        customer.setId(id);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(customer);

        customerService.deleteCustomerById(id);

        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingCustomer(){
        Long id = 1L;

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {customerService.deleteCustomerById(id);});

        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, never()).delete(any());
    }
}
