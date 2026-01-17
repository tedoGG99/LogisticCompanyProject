package com.nbu.logistics.repository;

import com.company.logistics.entity.DeliveryType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryTypeRepository extends JpaRepository<DeliveryType, Integer> {
}