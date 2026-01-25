package com.company.logistics.service;

import com.company.logistics.dto.ShipmentCreateRequest;
import com.company.logistics.dto.ShipmentResponse;

public interface ShipmentService {

    ShipmentResponse createShipment(ShipmentCreateRequest request);

    ShipmentResponse getByTrackingNumber(Integer trackingNumber);
}