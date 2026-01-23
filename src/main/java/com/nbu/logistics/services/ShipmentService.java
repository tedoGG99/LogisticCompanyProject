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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tedi
 */
@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private DeliveryTypeRepository deliveryTypeRepository; // You need this repo too
    @Autowired
    private ShipmentStatusRepository shipmentStatusRepository; // And this one

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
        DeliveryType type = deliveryTypeRepository.findByTypeName(dto.getDeliveryType()).orElse(null);
        if (type == null) {
            Logger.getLogger(ShipmentService.class.getName()).log(Level.SEVERE, "Cannot find Type");
            return;
        }
        shipment.setDeliveryTypeId(type);

        if ("TO_OFFICE".equals(dto.getDeliveryType())) {
            Office office = officeRepository.findById(dto.getTargetOfficeId()).orElse(null);
            shipment.setOffice(office);
        } else {
            shipment.setDeliveryAddress(dto.getTargetAddress());
        }

        // Set Initial Status
        ShipmentStatus status = shipmentStatusRepository.findByStatusName("REGISTERED").orElse(null);
        if (status != null) {
            shipment.setStatus(status);

            // CALCULATE PRICE [cite: 7, 8]
            // Logic: (Weight * Factor) + (If Address ? Surcharge : 0)
            BigDecimal price = BigDecimal.valueOf(dto.getWeight()).multiply(WEIGHT_FACTOR).add(BASE_PRICE);
            if ("TO_ADDRESS".equals(dto.getDeliveryType())) {
                price = price.add(ADDRESS_SURCHARGE);
            }
            shipment.setPrice(price);

            shipmentRepository.save(shipment);

        } else {
            Logger.getLogger(ShipmentService.class.getName()).log(Level.SEVERE, "Can not find status 'Registered'");
        }
    }

    // VISIBILITY LOGIC
    public List<Shipment> findShipmentsForUser(User user) {
        String roleName = "";

        if (roleName.equals("ROLE_OFFICE_EMPLOYEE") || roleName.equals("ROLE_COURIER") || roleName.equals("ROLE_ADMIN")) {
            return shipmentRepository.findAll();
        } else {
            List<Shipment> sent = shipmentRepository.findBySender(user);
            List<Shipment> received = shipmentRepository.findByReceiver(user);
            sent.addAll(received);
            return sent;
        }
    }

    // 1. UPDATE STATUS
    public void updateStatus(int shipmentId, String newStatusName) {
        Shipment shipment = shipmentRepository.findById(shipmentId).orElseThrow();
        ShipmentStatus status = shipmentStatusRepository.findByStatusName(newStatusName).orElseThrow();

        shipment.setStatus(status);

        // If delivered, set the date
        if ("DELIVERED".equals(newStatusName)) {
            shipment.setDateDelivered(LocalDateTime.now());
        }

        shipmentRepository.save(shipment);
    }

    // 2. DELETE
    public void deleteShipment(int id) {
        shipmentRepository.deleteById(id);
    }

    // 3. REPORT: FIND BY EMPLOYEE (Assuming employee is linked to shipment somehow, usually by Office)
    public List<Shipment> findShipmentsByEmployee(int employeeId) {
        // Logic: Find shipments where sender/receiver deals with this employee?
        // OR simpler: Find shipments registered by this employee (requires 'registeredBy' field in Entity)
        // For this assignment, we can return shipments sent FROM the office where the employee works:
        User employee = userRepository.findById(employeeId).orElseThrow();
        if (employee.getOffice() != null) {
            return shipmentRepository.findByOffice(employee.getOffice());
        }
        return List.of();
    }

    // 4. REPORT: FIND BY CLIENT
    public List<Shipment> findShipmentsByClient(int clientId) {
        User client = userRepository.findById(clientId).orElseThrow();
        List<Shipment> sent = shipmentRepository.findBySender(client);
        List<Shipment> received = shipmentRepository.findByReceiver(client);
        sent.addAll(received);
        return sent;
    }

    // 5. REPORT: UNRECEIVED
    public List<Shipment> findUnreceivedShipments() {
        // Custom Query in Repository needed
        return shipmentRepository.findAllUnreceivedShipments();
    }

    // 6. REPORT: REVENUE
    public BigDecimal calculateRevenue(LocalDate start, LocalDate end) {
        // Convert LocalDate to LocalDateTime for DB query
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        BigDecimal total = shipmentRepository.calculateRevenue(startDateTime, endDateTime);
        return total != null ? total : BigDecimal.ZERO;
    }

}
