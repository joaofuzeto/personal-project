package com.project.sales_api.controller;

import com.project.sales_api.dto.CustomerRequestDTO;
import com.project.sales_api.dto.CustomerResponseDTO;
import com.project.sales_api.service.impl.CustomerServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    CustomerServiceImpl customerServiceImpl;

    public CustomerController(CustomerServiceImpl customerServiceImpl) {
        this.customerServiceImpl = customerServiceImpl;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO){
        var customer = customerServiceImpl.createCustomer(customerRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> findCustomerById(@PathVariable("customerId") Long customerId){
        var customer = customerServiceImpl.findCustomerByID(customerId);

        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> listAllCustomers(){
        var customers = customerServiceImpl.findAllCustomers();

        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> updateUserById(@PathVariable("customerId") Long id, @Valid @RequestBody CustomerRequestDTO customerRequestDTO){
        
        CustomerResponseDTO updatedCustomer = customerServiceImpl.updateCustomer(id, customerRequestDTO);

        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomerByID(@PathVariable("customerId") Long userId){
        customerServiceImpl.deleteCustomerById(userId);

        return ResponseEntity.noContent().build();
    }
}
