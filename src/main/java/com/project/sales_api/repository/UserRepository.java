package com.project.sales_api.repository;

import com.project.sales_api.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
}
