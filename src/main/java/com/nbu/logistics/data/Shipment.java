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
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private Long id;

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

    
    @Column(nullable = false)
    private int deliveryTypeId; // Enum: TO_OFFICE, TO_ADDRESS

    // If TO_OFFICE, this must be set
    @ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;

    // If TO_ADDRESS, this must be set
    private String deliveryAddress;

    @Column(nullable = false)
    private ShipmentStatus statusId; // Enum: REGISTERED, SENT, RECEIVED, DELIVERED
    
    
    
}
