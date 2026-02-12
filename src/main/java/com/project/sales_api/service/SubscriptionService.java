package com.project.sales_api.service;

import com.project.sales_api.dto.SubscriptionRequestDTO;
import com.project.sales_api.dto.SubscriptionResponseDTO;

import java.util.List;

public interface SubscriptionService {

    SubscriptionResponseDTO createSubscription(SubscriptionRequestDTO subscriptionRequestDTO);
    SubscriptionResponseDTO findSubscriptionById(Long id);
    List<SubscriptionResponseDTO> findAllSubscriptions();
    SubscriptionResponseDTO updateSubscriptionById(Long id, SubscriptionRequestDTO subscriptionRequestDTO);
    void deleteSubscriptionById(Long id);
}
