package com.company.logistics.repository;

import com.company.logistics.entity.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentStatusRepository extends JpaRepository<ShipmentStatus, Integer> {
}