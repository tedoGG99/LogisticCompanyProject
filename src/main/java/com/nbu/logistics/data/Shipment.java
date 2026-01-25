//   **************************************************************************
//   * @ProjectName_____________________________________@    Version: @VerNr@ *
//   *                                                                        *
//   * This software is the proprietary information of STRATEGY OBJECT.       *
//   * Use is subject to license terms.                                       *
//   *                                                                        *
//   * Copyright (c) 1997-2025 STRATEGY OBJECT                                *
//   * All rights reserved.                           @VersionDate__________@ *
//   **************************************************************************
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbu.logistics.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author Teodor Georgiev
 */
@Entity
@Table(name = "shipments")
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Use a String for tracking numbers (e.g., UUIDs) to avoid running out of integers
    @Column(nullable = false, unique = true)
    private String trackingNumber;

    @Column(nullable = false)
    private Double weight;

    // Requirement: Price depends on weight and delivery type 
    // We store the calculated price here so it doesn't change if tariffs change later.
    @Column(nullable = false)
    private BigDecimal price;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dateRegistered;

    private LocalDateTime dateDelivered;

    // RELATIONS:

    // Requirement: Clients can see shipments they SENT 
    // We need a link to the User entity for the sender.
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    // Receiver can be a User (if registered) or just a name (if guest)
    // For this assignment, linking to User is safer for "My Shipments" logic.
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    // If receiver is not a registered user, store details here (optional based on logic)
    private String receiverName;
    private String receiverPhone;

    // LOGIC FOR DESTINATION [cite: 7, 8]
    // A shipment is EITHER to an Office OR to an Address.

    
    @ManyToOne
    @JoinColumn(name = "delivery_type_id", nullable = false)
    private DeliveryType deliveryTypeId; // Enum: TO_OFFICE, TO_ADDRESS

    // If TO_OFFICE, this must be set
    @ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;

    // If TO_ADDRESS, this must be set
    private String deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private ShipmentStatus status; // Enum: REGISTERED, SENT, RECEIVED, DELIVERED
    
    @ManyToOne
    @JoinColumn(name = "employee_id") // This creates an 'employee_id' column in your DB
    private User employee;

    public Shipment() {
    }

    public Shipment(Integer id, String trackingNumber, Double weight, BigDecimal price, LocalDateTime dateRegistered, LocalDateTime dateDelivered, User sender, User receiver, String receiverName, String receiverPhone, DeliveryType deliveryTypeId, Office office, String deliveryAddress, ShipmentStatus status, User employee) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.weight = weight;
        this.price = price;
        this.dateRegistered = dateRegistered;
        this.dateDelivered = dateDelivered;
        this.sender = sender;
        this.receiver = receiver;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.deliveryTypeId = deliveryTypeId;
        this.office = office;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.employee = employee;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDateTime dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public LocalDateTime getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(LocalDateTime dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public DeliveryType getDeliveryTypeId() {
        return deliveryTypeId;
    }

    public void setDeliveryTypeId(DeliveryType deliveryTypeId) {
        this.deliveryTypeId = deliveryTypeId;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus statusId) {
        this.status = statusId;
    }
    
    
    
}
