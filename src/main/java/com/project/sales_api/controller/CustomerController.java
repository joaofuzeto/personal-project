package com.project.sales_api.controller;

import com.project.sales_api.dto.CustomerRequestDTO;
import com.project.sales_api.entity.Customer;
import com.project.sales_api.service.impl.CustomerServiceImpl;
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
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerRequestDTO customerRequestDTO){
        var customerId = customerServiceImpl.createCustomer(customerRequestDTO);

        return ResponseEntity.created(URI.create("/v1/users/" + customerId.toString())).build();
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
