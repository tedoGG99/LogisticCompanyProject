package com.nbu.logistics.controller;

import com.company.logistics.dto.ShipmentCreateRequest;
import com.company.logistics.dto.ShipmentResponse;
import com.company.logistics.service.ShipmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    // CREATE shipment
    @PostMapping
    public ResponseEntity<ShipmentResponse> createShipment(
            @Valid @RequestBody ShipmentCreateRequest request) {

        ShipmentResponse response = shipmentService.createShipment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET shipment by tracking number
    @GetMapping("/{trackingNumber}")
    public ResponseEntity<ShipmentResponse> getByTrackingNumber(
            @PathVariable Integer trackingNumber) {

        ShipmentResponse response =
                shipmentService.getByTrackingNumber(trackingNumber);

        return ResponseEntity.ok(response);
    }
}       // За сега работи с Entity, но ще го направим да работи с DTO като направим логиката