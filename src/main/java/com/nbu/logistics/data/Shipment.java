package com.company.logistics.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Shipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TrackingNumber", nullable = false, unique = true)
    private Integer trackingNumber;

    @Column(name = "Weight", nullable = false)
    private Double weight;

    @Column(name = "DateRegistered", nullable = false)
    private LocalDate dateRegistered;

    @Column(name = "DateDelivered")
    private LocalDate dateDelivered;

    @Column(name = "ReceiverName", nullable = false)
    private String receiverName;

    @Column(name = "ReceiverPhone")
    private String receiverPhone;

    @Column(name = "Shipment_Status_ID", nullable = false)
    private Integer shipmentStatusId;

    @Column(name = "DeliveryType_ID", nullable = false)
    private Integer deliveryTypeId;

    @Column(name = "Address_Client_Delivery_ID", nullable = false)
    private Integer clientAddressId;

    @Column(name = "Address_Office_Delivery_ID", nullable = false)
    private Integer officeAddressId;

    @Column(name = "tariff_id", nullable = false)
    private Integer tariffId;
}
