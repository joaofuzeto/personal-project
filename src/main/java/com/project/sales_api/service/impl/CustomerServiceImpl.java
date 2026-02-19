package com.project.sales_api.service.impl;

import com.project.sales_api.dto.CustomerRequestDTO;
import com.project.sales_api.dto.CustomerResponseDTO;
import com.project.sales_api.entity.Customer;
import com.project.sales_api.exception.CustomerNotFoundException;
import com.project.sales_api.repository.CustomerRepository;
import com.project.sales_api.service.CustomerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setDocument(dto.document());
        customer.setCreatedAt(LocalDateTime.now());

        var customerSaved = customerRepository.save(customer);

        return toDto(customerSaved);
    }

    @Override
    public CustomerResponseDTO findCustomerByID(Long customerId) {

        var customerFound = customerRepository.findById(customerId)
                .orElseThrow(CustomerNotFoundException::new);

        return toDto(customerFound);
    }

    @Override
    public List<CustomerResponseDTO> findAllCustomers() {

        return customerRepository
                .findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO dto) {
        var customerEntity = customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);

        if(dto.name() != null){
            customerEntity.setName(dto.name());
        }
        if(dto.email() != null){
            customerEntity.setEmail(dto.email());
        }
        if(dto.document() != null){
            customerEntity.setDocument(dto.document());
        }
        customerRepository.save(customerEntity);

        return toDto(customerEntity);

    }

    @Override
    public void deleteCustomerById(Long id) {
        var customerExist = customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);

        customerRepository.delete(customerExist);
    }

    private CustomerResponseDTO toDto(Customer c){
        return new CustomerResponseDTO(
                c.getName(),
                c.getEmail(),
                c.getDocument());
    }
}
