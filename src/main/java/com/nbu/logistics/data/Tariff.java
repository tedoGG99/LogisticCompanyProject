package com.company.logistics.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tariff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtable1")
    private Integer id;

    @Column(name = "BasePrice", nullable = false)
    private BigDecimal basePrice;

    @Column(name = "PricePerKg", nullable = false)
    private BigDecimal pricePerKg;

    @Column(name = "ValidFrom", nullable = false)
    private LocalDate validFrom;

    @Column(name = "ValidTo", nullable = false)
    private LocalDate validTo;

    @Column(name = "DeliveryType_ID", nullable = false)
    private Integer deliveryTypeId;
}