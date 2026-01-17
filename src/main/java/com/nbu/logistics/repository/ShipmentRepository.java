package com.company.logistics.repository;

import com.company.logistics.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {

    Optional<Shipment> findByTrackingNumber(Integer trackingNumber);

    List<Shipment> findByShipmentStatusId(Integer shipmentStatusId);

    List<Shipment> findByDateRegisteredBetween(
            LocalDate start,
            LocalDate end
    );

    List<Shipment> findByDeliveryTypeId(Integer deliveryTypeId);
}