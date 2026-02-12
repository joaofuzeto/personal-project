package com.project.sales_api.service.impl;

import com.project.sales_api.Enums.SubscriptionStatus;
import com.project.sales_api.dto.SubscriptionRequestDTO;
import com.project.sales_api.dto.SubscriptionResponseDTO;
import com.project.sales_api.entity.Subscription;
import com.project.sales_api.exception.ResourceNotFoundException;
import com.project.sales_api.repository.CustomerRepository;
import com.project.sales_api.repository.SubscriptionRepository;
import com.project.sales_api.service.SubscriptionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CustomerRepository customerRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, CustomerRepository customerRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public SubscriptionResponseDTO createSubscription(SubscriptionRequestDTO subscriptionRequestDTO) {

        var customer = customerRepository.findById(subscriptionRequestDTO.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        var subscriptionEntity = new Subscription();
        subscriptionEntity.setPlanName(subscriptionRequestDTO.planName());
        subscriptionEntity.setPrice(subscriptionRequestDTO.price());
        subscriptionEntity.setStatus(SubscriptionStatus.ACTIVE);
        subscriptionEntity.setCustomer(customer);
        subscriptionEntity.setStartDate(LocalDate.now());
        subscriptionEntity.setEndDate(LocalDate.now().plusMonths(2));

        subscriptionRepository.save(subscriptionEntity);

        return toDto(subscriptionEntity);
    }

    @Override
    public SubscriptionResponseDTO findSubscriptionById(Long id) {
        var subscriptionFound = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada."));

        return toDto(subscriptionFound);
    }

    @Override
    public List<SubscriptionResponseDTO> findAllSubscriptions() {
        return subscriptionRepository.findAll().stream().map(
                        this::toDto)
                .toList();
    }

    @Override
    public SubscriptionResponseDTO updateSubscriptionById(Long id, SubscriptionRequestDTO subscriptionRequestDTO) {
        var subscriptionEntity = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada"));

        if(subscriptionRequestDTO.planName() != null){
            subscriptionEntity.setPlanName(subscriptionRequestDTO.planName());
        }
        if(subscriptionRequestDTO.price() != null){
            subscriptionEntity.setPrice(subscriptionRequestDTO.price());
        }

        subscriptionRepository.save(subscriptionEntity);

        return toDto(subscriptionEntity);
    }

    @Override
    public void deleteSubscriptionById(Long id) {
        var subscriptionExist = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada"));

        subscriptionRepository.delete(subscriptionExist);
    }

    private SubscriptionResponseDTO toDto(Subscription s){
        return new SubscriptionResponseDTO(
                s.getId(),
                s.getPlanName(),
                s.getPrice(),
                s.getStatus(),
                s.getCustomer().getName());
    }
}
