package com.project.sales_api.service;

import com.project.sales_api.dto.CustomerRequestDTO;
import com.project.sales_api.dto.CustomerResponseDTO;
import com.project.sales_api.entity.Customer;

import java.util.List;

public interface CustomerService {

    Long create(CustomerRequestDTO dto);
    CustomerResponseDTO findByID(Long id);
    List<CustomerResponseDTO> findAll();
    CustomerResponseDTO update(Long id, CustomerRequestDTO dto);
    void delete(Long id);
}
