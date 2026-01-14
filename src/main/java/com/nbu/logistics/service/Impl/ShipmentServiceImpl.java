package com.company.logistics.service.impl;

import com.company.logistics.dto.ShipmentCreateRequest;
import com.company.logistics.dto.ShipmentResponse;
import com.company.logistics.entity.Shipment;
import com.company.logistics.repository.ShipmentRepository;
import com.company.logistics.service.ShipmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    @Override
    public ShipmentResponse createShipment(ShipmentCreateRequest request) {

        Shipment shipment = new Shipment();
        shipment.setId(generateId());
        shipment.setTrackingNumber(generateTrackingNumber());
        shipment.setWeight(request.getWeight());
        shipment.setReceiverName(request.getReceiverName());
        shipment.setReceiverPhone(request.getReceiverPhone());
        shipment.setDateRegistered(LocalDate.now());

        shipment.setShipmentStatusId(1); // REGISTERED
        shipment.setDeliveryTypeId(request.getDeliveryTypeId());
        shipment.setClientAddressId(request.getClientAddressId());
        shipment.setOfficeAddressId(request.getOfficeAddressId());

        shipment.setTariffId(1); // временно

        Shipment saved = shipmentRepository.save(shipment);

        return new ShipmentResponse(
                saved.getId(),
                saved.getTrackingNumber(),
                saved.getWeight(),
                saved.getReceiverName(),
                "REGISTERED"
        );
    }

    @Override
    public ShipmentResponse getByTrackingNumber(Integer trackingNumber) {

        Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        return new ShipmentResponse(
                shipment.getId(),
                shipment.getTrackingNumber(),
                shipment.getWeight(),
                shipment.getReceiverName(),
                "STATUS_ID=" + shipment.getShipmentStatusId()
        );
    }

    // --- helpers ---
    private Integer generateId() {
        return Math.abs(UUID.randomUUID().hashCode());
    }

    private Integer generateTrackingNumber() {
        return Math.abs(UUID.randomUUID().hashCode());
    }
}
