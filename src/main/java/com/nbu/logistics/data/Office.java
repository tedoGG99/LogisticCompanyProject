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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

/**
 *
 * @author Teodor Georgiev
 */
@Entity
@Table(name = "offices")
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // e.g., "Sofia Central", "Plovdiv North"
    @Column(nullable = false)
    private String name;

    // Grouping by city allows for easier filtering later
    @Column(nullable = false)
    private String city;

    // Full physical address
    @Column(nullable = false)
    private String address;

    // RELATIONS

    // 1. One Office has many Shipments delivered to it
    // "mappedBy" refers to the 'office' field in the Shipment entity
    @OneToMany(mappedBy = "office")
    private List<Shipment> shipments;

    // 2. One Office has many Employees working there
    // You will need to add an 'office' field to your User entity for this to work
    @OneToMany(mappedBy = "office")
    private List<User> employees;
    
    // Helper method to display office info easily in dropdowns (Thymeleaf)
    public String getFullLocation() {
        return city + " - " + address;
    }

    public Office() {
    }

    public Office(int id, String name, String city, String address, List<Shipment> shipments, List<User> employees) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.shipments = shipments;
        this.employees = employees;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }

    public List<User> getEmployees() {
        return employees;
    }

    public void setEmployees(List<User> employees) {
        this.employees = employees;
    }
    
    
}
