package com.project.sales_api.service.impl;

import com.project.sales_api.Enums.SubscriptionStatus;
import com.project.sales_api.dto.SubscriptionRequestDTO;
import com.project.sales_api.dto.SubscriptionResponseDTO;
import com.project.sales_api.entity.Subscription;
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
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        var subscriptionEntity = new Subscription();
        subscriptionEntity.setPlanName(subscriptionRequestDTO.planName());
        subscriptionEntity.setPrice(subscriptionRequestDTO.price());
        subscriptionEntity.setStatus(subscriptionRequestDTO.subscriptionStatus());
        subscriptionEntity.setCustomer(customer);
        subscriptionEntity.setStartDate(LocalDate.now());
        subscriptionEntity.setEndDate(LocalDate.now().plusMonths(2));

        subscriptionRepository.save(subscriptionEntity);

        return new SubscriptionResponseDTO(
                subscriptionEntity.getPlanName(),
                subscriptionEntity.getPrice(),
                subscriptionEntity.getStatus(),
                subscriptionEntity.getCustomer().getName());
    }

    @Override
    public SubscriptionResponseDTO findSubscriptionById(Long id) {
        var subscriptionFound = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assinatura não encontrada."));

        return new SubscriptionResponseDTO(
                subscriptionFound.getPlanName(),
                subscriptionFound.getPrice(),
                subscriptionFound.getStatus(),
                subscriptionFound.getCustomer().getName());
    }

    @Override
    public List<SubscriptionResponseDTO> findAllSubscriptions() {
        return subscriptionRepository.findAll().stream().map(
                s -> new SubscriptionResponseDTO(s.getPlanName(), s.getPrice(), s.getStatus(), s.getCustomer().getName()))
                .toList();
    }

    @Override
    public SubscriptionResponseDTO updateSubscriptionById(Long id, SubscriptionRequestDTO subscriptionRequestDTO) {
        var subscriptionFound = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assinatura não encontrada"));

        if(subscriptionRequestDTO.planName() != null){
            subscriptionFound.setPlanName(subscriptionRequestDTO.planName());
        }
        if(subscriptionRequestDTO.price() != null){
            subscriptionFound.setPrice(subscriptionRequestDTO.price());
        }
        if(subscriptionRequestDTO.subscriptionStatus() != null){
            if(subscriptionFound.getEndDate().isBefore(LocalDate.now())) {
                subscriptionFound.setStatus(SubscriptionStatus.EXPIRED);
            } else{
                subscriptionFound.setStatus(subscriptionRequestDTO.subscriptionStatus());
            }
        }
        return new SubscriptionResponseDTO(subscriptionFound.getPlanName(), subscriptionFound.getPrice(), subscriptionFound.getStatus(), subscriptionFound.getCustomer().getName());
    }

    @Override
    public void deleteSubscriptionById(Long id) {
        var subscriptionExist = subscriptionRepository.existsById(id);

        if(subscriptionExist){
            subscriptionRepository.deleteById(id);
        } else{
            throw new RuntimeException("Assinatura não existe");
        }
    }
}
