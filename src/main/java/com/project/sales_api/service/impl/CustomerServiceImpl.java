package com.project.sales_api.service.impl;

import com.project.sales_api.dto.CustomerRequestDTO;
import com.project.sales_api.dto.CustomerResponseDTO;
import com.project.sales_api.entity.Customer;
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
    public Long create(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setDocument(dto.document());
        customer.setCreatedAt(LocalDateTime.now());

        var customerSaved = customerRepository.save(customer);

        return customerSaved.getId();
    }

    @Override
    public CustomerResponseDTO findByID(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return new CustomerResponseDTO(customer.getId(), customer.getName(), customer.getEmail(), customer.getDocument());
    }

    @Override
    public List<CustomerResponseDTO> findAll() {
        return List.of();
    }

    @Override
    public CustomerResponseDTO update(Long id, CustomerRequestDTO dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
