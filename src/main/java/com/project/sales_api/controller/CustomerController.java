package com.project.sales_api.controller;

import com.project.sales_api.dto.CustomerRequestDTO;
import com.project.sales_api.entity.Customer;
import com.project.sales_api.service.impl.CustomerServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    CustomerServiceImpl customerServiceImpl;

    public CustomerController(CustomerServiceImpl customerServiceImpl) {
        this.customerServiceImpl = customerServiceImpl;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerRequestDTO customerRequestDTO){
        var customerId = customerServiceImpl.create(customerRequestDTO);

        return ResponseEntity.created(URI.create("/v1/users/" + customerId.toString())).build();
    }

}
