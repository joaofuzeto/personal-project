package com.project.sales_api.service;

import com.project.sales_api.dto.CustomerRequestDTO;
import com.project.sales_api.dto.CustomerResponseDTO;
import com.project.sales_api.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    CustomerResponseDTO createCustomer(CustomerRequestDTO dto);
    CustomerResponseDTO findCustomerByID(Long id);
    List<CustomerResponseDTO> findAllCustomers();
    CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO dto);
    void deleteCustomerById(Long id);
}
