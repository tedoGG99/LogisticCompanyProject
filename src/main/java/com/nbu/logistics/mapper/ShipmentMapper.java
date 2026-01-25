package com.nbu.logistics.mapper;

import com.company.logistics.dto.ShipmentCreateRequest;
import com.company.logistics.dto.ShipmentResponse;
import com.company.logistics.entity.Shipment;

import java.time.LocalDate;

public class ShipmentMapper {

    private ShipmentMapper() {
    }

    public static Shipment toEntity(
            ShipmentCreateRequest request,
            Integer id,
            Integer trackingNumber,
            Integer statusId,
            Integer tariffId
    ) {
        Shipment shipment = new Shipment();
        shipment.setId(id);
        shipment.setTrackingNumber(trackingNumber);
        shipment.setWeight(request.getWeight());
        shipment.setReceiverName(request.getReceiverName());
        shipment.setReceiverPhone(request.getReceiverPhone());
        shipment.setDateRegistered(LocalDate.now());
        shipment.setShipmentStatusId(statusId);
        shipment.setDeliveryTypeId(request.getDeliveryTypeId());
        shipment.setClientAddressId(request.getClientAddressId());
        shipment.setOfficeAddressId(request.getOfficeAddressId());
        shipment.setTariffId(tariffId);
        return shipment;
    }

    public static ShipmentResponse toResponse(Shipment shipment, String statusName) {
        return new ShipmentResponse(
                shipment.getId(),
                shipment.getTrackingNumber(),
                shipment.getWeight(),
                shipment.getReceiverName(),
                statusName
        );
    }
}