package com.project.sales_api.controller;

import com.project.sales_api.dto.SubscriptionRequestDTO;
import com.project.sales_api.dto.SubscriptionResponseDTO;
import com.project.sales_api.service.impl.SubscriptionServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/v1/subscriptions")
public class SubscriptionController {

    SubscriptionServiceImpl subscriptionServiceImpl;

    public SubscriptionController(SubscriptionServiceImpl subscriptionServiceImpl) {
        this.subscriptionServiceImpl = subscriptionServiceImpl;
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponseDTO> createSubscription(@Valid @RequestBody SubscriptionRequestDTO subscriptionRequestDTO){
        var subscriptionCreated = subscriptionServiceImpl.createSubscription(subscriptionRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionCreated);
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponseDTO> findSubscriptionById(@PathVariable("subscriptionId") Long id){
        var subscriptionFound = subscriptionServiceImpl.findSubscriptionById(id);

        return ResponseEntity.ok(subscriptionFound);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDTO>> findAllSubscriptions(){
        var subscriptionList = subscriptionServiceImpl.findAllSubscriptions();

        return ResponseEntity.ok(subscriptionList);
    }

    @PutMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponseDTO> updatingSubscriptionById(@PathVariable("subscriptionId") Long id, @Valid @RequestBody SubscriptionRequestDTO subscriptionRequestDTO){
        var subscriptionUpdated = subscriptionServiceImpl.updateSubscriptionById(id, subscriptionRequestDTO);

        return ResponseEntity.ok(subscriptionUpdated);
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<Void> deleteSubscriptionById(@PathVariable("subscriptionId") Long id){
        subscriptionServiceImpl.deleteSubscriptionById(id);

        return ResponseEntity.noContent().build();
    }

}
