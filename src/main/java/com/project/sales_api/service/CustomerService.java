package com.project.sales_api.service;

import com.project.sales_api.dto.CustomerRequestDTO;
import com.project.sales_api.dto.CustomerResponseDTO;
import com.project.sales_api.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    String createCustomer(CustomerRequestDTO dto);
    Optional<Customer> findCustomerByID(Long id);
    List<CustomerResponseDTO> findAll();
    CustomerResponseDTO update(Long id, CustomerRequestDTO dto);
    void delete(Long id);
}
