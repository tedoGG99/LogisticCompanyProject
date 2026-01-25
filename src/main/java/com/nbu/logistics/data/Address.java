package com.company.logistics.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "Address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "City", nullable = false)
    private String city;

    @Column(name = "PostalCode", nullable = false)
    private String postalCode;

    @Column(name = "Street", nullable = false)
    private String street;

    @Column(name = "Number")
    private Integer number;

    @Column(name = "Details")
    private String details;
}