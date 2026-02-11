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
import java.util.Optional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    public String createCustomer(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setDocument(dto.document());
        customer.setCreatedAt(LocalDateTime.now());

        var customerSaved = customerRepository.save(customer);

        return "O usuário " + customerSaved.getName() + " foi criado com sucesso.";
    }

    @Override
    public Optional<Customer> findCustomerByID(Long customerId) {
        return customerRepository.findById(customerId);
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
