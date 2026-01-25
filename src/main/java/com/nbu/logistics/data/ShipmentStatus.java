package com.company.logistics.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "Shipment_Status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentStatus {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "Status", nullable = false)
    private String status;
}