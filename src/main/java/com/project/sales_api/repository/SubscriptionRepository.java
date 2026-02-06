package com.project.sales_api.repository;

import com.project.sales_api.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
