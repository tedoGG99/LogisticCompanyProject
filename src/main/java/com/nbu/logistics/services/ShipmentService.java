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

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private DeliveryTypeRepository deliveryTypeRepository;

    @Autowired
    private ShipmentStatusRepository shipmentStatusRepository;

    @Autowired
    private PricingService pricingService;

    /**
     * Creates a new Shipment based on the DTO.
     */
    public void createShipment(ShipmentDto dto, User creator) {
        Shipment shipment = new Shipment();

        // ---------------------------------------------------------
        // 1. BASIC SETUP
        // ---------------------------------------------------------
        shipment.setTrackingNumber(UUID.randomUUID().toString());
        shipment.setDateRegistered(LocalDateTime.now());
        shipment.setWeight(dto.getWeight());

        // ---------------------------------------------------------
        // 2. SENDER & AUDIT LOGIC
        // ---------------------------------------------------------
        boolean isStaff = creator.getRole() != null && Boolean.TRUE.equals(creator.getRole().getIsStaff());

        if (isStaff) {
            // Employee registers shipment for a customer
            User sender = userRepository.findById(dto.getSenderId())
                    .orElseThrow(() -> new IllegalArgumentException("Sender ID not found"));

            shipment.setSender(sender);
            shipment.setEmployee(creator);
        } else {
            // Client at home: ignore senderId
            shipment.setSender(creator);
        }

        // ✅ Sender contact email (from DTO or fallback to sender user email)
        if (dto.getSenderEmail() != null && !dto.getSenderEmail().trim().isEmpty()) {
            shipment.setSenderEmail(dto.getSenderEmail().trim());
        } else {
            shipment.setSenderEmail(shipment.getSender().getEmail());
        }

        // ---------------------------------------------------------
        // 3. RECEIVER LOGIC (Registered OR Manual)
        // ---------------------------------------------------------
        String receiverUsername = dto.getReceiverUsername();
        String receiverManualUsername = dto.getReceiverManualUsername();
        String receiverEmail = dto.getReceiverEmail();

        // CASE A: Registered receiver selected
        if (receiverUsername != null && !receiverUsername.trim().isEmpty()) {

            User receiver = userRepository.findByUsername(receiverUsername.trim())
                    .orElseThrow(() -> new IllegalArgumentException("Receiver username not found"));

            shipment.setReceiver(receiver);

            // receiver display name
            if (dto.getReceiverName() == null || dto.getReceiverName().trim().isEmpty()) {
                shipment.setReceiverName(receiver.getUsername());
            } else {
                shipment.setReceiverName(dto.getReceiverName());
            }

            // ✅ Force receiverEmail from DB
            shipment.setReceiverEmail(receiver.getEmail());

            // ✅ Manual guest username irrelevant in this case
            shipment.setReceiverUsername(null);

        } else {
            // CASE B: Manual / guest receiver
            if (receiverEmail == null || receiverEmail.trim().isEmpty()) {
                throw new IllegalArgumentException("Receiver email is required for guest receiver");
            }

            shipment.setReceiver(null); // no account
            shipment.setReceiverName(dto.getReceiverName());
            shipment.setReceiverEmail(receiverEmail.trim());

            // optional manual username
            if (receiverManualUsername != null && !receiverManualUsername.trim().isEmpty()) {
                shipment.setReceiverUsername(receiverManualUsername.trim());
            } else {
                shipment.setReceiverUsername(null);
            }
        }

        // ✅ Phone validation (recommended)
        if (dto.getReceiverPhone() == null || dto.getReceiverPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Receiver phone is required");
        }
        shipment.setReceiverPhone(dto.getReceiverPhone().trim());

        // ---------------------------------------------------------
        // 4. DELIVERY TYPE LOGIC
        // ---------------------------------------------------------
        DeliveryType type = deliveryTypeRepository.findById(dto.getDeliveryTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Delivery Type ID"));

        shipment.setDeliveryTypeId(type);

        if (Boolean.TRUE.equals(type.getRequiresOffice())) {
            if (dto.getTargetOfficeId() == null) {
                throw new IllegalArgumentException("Office ID is required for this delivery type");
            }

            Office office = officeRepository.findById(dto.getTargetOfficeId())
                    .orElseThrow(() -> new IllegalArgumentException("Office not found"));

            shipment.setOffice(office);
            shipment.setDeliveryAddress(office.getFullLocation());

        } else {
            if (dto.getTargetAddress() == null || dto.getTargetAddress().trim().isEmpty()) {
                throw new IllegalArgumentException("Target Address is required for this delivery type");
            }
            shipment.setDeliveryAddress(dto.getTargetAddress().trim());
        }

        // ---------------------------------------------------------
        // 5. INITIAL STATUS
        // ---------------------------------------------------------
        ShipmentStatus initialStatus = shipmentStatusRepository.findFirstByIsInitialTrue()
                .orElseThrow(() -> new IllegalStateException(
                        "System Config Error: No status marked as 'is_initial' in DB"));

        shipment.setStatus(initialStatus);

        // ---------------------------------------------------------
        // 6. PRICING LOGIC
        // ---------------------------------------------------------
        BigDecimal basePrice = pricingService.getBasePrice();
        BigDecimal weightFactor = pricingService.getWeightFactor();
        BigDecimal surcharge = pricingService.getAddressSurcharge();

        BigDecimal price = BigDecimal.valueOf(dto.getWeight()).multiply(weightFactor).add(basePrice);

        if (!Boolean.TRUE.equals(type.getRequiresOffice())) {
            price = price.add(surcharge);
        }

        shipment.setPrice(price);

        // ---------------------------------------------------------
        // 7. FINAL SAVE
        // ---------------------------------------------------------
        shipmentRepository.save(shipment);
    }

    // VISIBILITY LOGIC
    public List<Shipment> findShipmentsForUser(User user) {
        String roleName = (user.getRole() != null) ? user.getRole().getRole() : null;

        if (roleName == null) {
            // Default: client behavior
            List<Shipment> sent = shipmentRepository.findBySender(user);
            List<Shipment> received = shipmentRepository.findByReceiver(user);
            sent.addAll(received);
            return sent;
        }

        switch (roleName) {
            case "ROLE_OFFICE_EMPLOYEE":
            case "ROLE_ADMIN":
                return shipmentRepository.findAll();

            case "ROLE_COURIER":
                return shipmentRepository.findByCourier(user);

            default:
                List<Shipment> sent = shipmentRepository.findBySender(user);
                List<Shipment> received = shipmentRepository.findByReceiver(user);
                sent.addAll(received);
                return sent;
        }
    }

    public void updateStatus(int shipmentId, String newStatusName, String username) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));

        ShipmentStatus status = shipmentStatusRepository.findByStatusName(newStatusName)
                .orElseThrow(() -> new IllegalArgumentException("Status not found: " + newStatusName));

        shipment.setStatus(status);

        if (shipment.getEmployee() == null && username != null) {
            User currentUser = userRepository.findByUsername(username).orElse(null);
            shipment.setEmployee(currentUser);
        }

        if ("DELIVERED".equalsIgnoreCase(newStatusName)) {
            shipment.setDateDelivered(LocalDateTime.now());
        }

        shipmentRepository.save(shipment);
    }

    // DELETE
    public void deleteShipment(int id) {
        shipmentRepository.deleteById(id);
    }

    // REPORT: REVENUE
    public BigDecimal calculateRevenue(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        BigDecimal total = shipmentRepository.calculateRevenue(startDateTime, endDateTime);
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<Shipment> findShipmentsByEmployee(int employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));

        if (employee.getOffice() == null) {
            throw new IllegalStateException("Employee is not assigned to any office");
        }

        return shipmentRepository.findByOffice(employee.getOffice());
    }

    /**
     * Report: Find all shipments that are NOT delivered.
     */
    public List<Shipment> findUnreceivedShipments() {
        return shipmentRepository.findAllUnreceivedShipments();
    }

    public List<Shipment> findShipmentsByClient(int clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with ID: " + clientId));

        List<Shipment> sent = shipmentRepository.findBySender(client);
        List<Shipment> received = shipmentRepository.findByReceiver(client);

        sent.addAll(received);
        return sent;
    }

    public Shipment getShipmentById(Integer id) {
        return shipmentRepository.findById(id).orElse(null);
    }
}
