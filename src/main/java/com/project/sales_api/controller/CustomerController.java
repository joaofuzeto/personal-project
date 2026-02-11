package com.project.sales_api.controller;

import com.project.sales_api.dto.CustomerRequestDTO;
import com.project.sales_api.entity.Customer;
import com.project.sales_api.service.impl.CustomerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    CustomerServiceImpl customerServiceImpl;

    public CustomerController(CustomerServiceImpl customerServiceImpl) {
        this.customerServiceImpl = customerServiceImpl;
    }

    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody CustomerRequestDTO customerRequestDTO){
        var customerId = customerServiceImpl.createCustomer(customerRequestDTO);
        String message = "O cliente " + customerRequestDTO.name() + " foi criado com sucesso";

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable("customerId") Long customerId){
        var customer = customerServiceImpl.findCustomerByID(customerId);
        if(customer.isPresent()){
            return ResponseEntity.ok(customer.get());
        } else{
            return ResponseEntity.notFound().build();
        }
    }



}
