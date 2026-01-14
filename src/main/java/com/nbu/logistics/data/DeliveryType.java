package com.company.logistics.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "DeliveryType")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryType {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "DeliveryType", nullable = false)
    private String deliveryType;
}