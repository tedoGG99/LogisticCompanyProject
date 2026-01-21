/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbu.logistics.services;

import com.nbu.logistics.data.DeliveryType;
import com.nbu.logistics.data.Office;
import com.nbu.logistics.data.Shipment;
import com.nbu.logistics.data.ShipmentStatus;
import com.nbu.logistics.data.User;
import com.nbu.logistics.dto.ShipmentDto;
import com.nbu.logistics.repositories.DeliveryTypeRepository;
import com.nbu.logistics.repositories.OfficeRepository;
import com.nbu.logistics.repositories.ShipmentRepository;
import com.nbu.logistics.repositories.ShipmentStatusRepository;
import com.nbu.logistics.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


/**
 *
 * @author tedi
 */

@Service
public class ShipmentService {
    
    @Autowired private ShipmentRepository shipmentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private OfficeRepository officeRepository;
    @Autowired private DeliveryTypeRepository deliveryTypeRepository; // You need this repo too
    @Autowired private ShipmentStatusRepository shipmentStatusRepository; // And this one

    
    // PRICING CONSTANTS [cite: 7, 8]
    private static final BigDecimal BASE_PRICE = new BigDecimal("5.00");
    private static final BigDecimal WEIGHT_FACTOR = new BigDecimal("2.00"); // Price per kg
    private static final BigDecimal ADDRESS_SURCHARGE = new BigDecimal("10.00"); // Extra cost for home delivery

    public void createShipment(ShipmentDto dto, User creator) {
        
        Shipment shipment = new Shipment();
        
        shipment.setTrackingNumber(UUID.randomUUID().toString());
        shipment.setDateRegistered(LocalDateTime.now());
        shipment.setWeight(dto.getWeight());

        // Set Sender (The Client)
        User sender = userRepository.findById(dto.getSenderId()).orElseThrow(() -> new RuntimeException("Sender not found"));
        shipment.setSender(sender);

        // Set Receiver (User or Guest details)
        if (dto.getReceiverUsername() != null && !dto.getReceiverUsername().isEmpty()) {
            User receiver = userRepository.findByUsername(dto.getReceiverUsername()).orElse(null);
            shipment.setReceiver(receiver);
        }
        shipment.setReceiverName(dto.getReceiverName());
        shipment.setReceiverPhone(dto.getReceiverPhone());

        // Set Delivery Type & Office/Address [cite: 7]
        // You would fetch these entities from their Repositories based on the String in DTO
        DeliveryType type = deliveryTypeRepository.findByTypeName(dto.getDeliveryType()); 
        shipment.setDeliveryTypeId(type.getId());

        if ("TO_OFFICE".equals(dto.getDeliveryType())) {
            Office office = officeRepository.findById(dto.getTargetOfficeId()).orElse(null);
            shipment.setOffice(office);
        } else {
            shipment.setDeliveryAddress(dto.getTargetAddress());
        }

        // Set Initial Status
        ShipmentStatus status = shipmentStatusRepository.findByStatusName("REGISTERED");
        shipment.setStatus(status);

        // CALCULATE PRICE [cite: 7, 8]
        // Logic: (Weight * Factor) + (If Address ? Surcharge : 0)
        BigDecimal price = BigDecimal.valueOf(dto.getWeight()).multiply(WEIGHT_FACTOR).add(BASE_PRICE);
        if ("TO_ADDRESS".equals(dto.getDeliveryType())) {
            price = price.add(ADDRESS_SURCHARGE);
        }
        shipment.setPrice(price);

        shipmentRepository.save(shipment);
    }

    // VISIBILITY LOGIC
    // Employees see ALL, Clients see THEIR OWN [cite: 9, 33, 34]
    public List<Shipment> findShipmentsForUser(User user) {
        String roleName = "";
        
        if (roleName.equals("ROLE_OFFICE_EMPLOYEE") || roleName.equals("ROLE_COURIER") || roleName.equals("ROLE_ADMIN")) {
            return shipmentRepository.findAll();
        } else {
            // It is a CLIENT: show sent AND received
            List<Shipment> sent = shipmentRepository.findBySender(user);
            List<Shipment> received = shipmentRepository.findByReceiver(user);
            sent.addAll(received); // Combine lists
            return sent;
        }
    }

}
