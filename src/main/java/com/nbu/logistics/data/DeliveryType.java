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
import jakarta.persistence.Table;

/**
 *
 * @author Teodor Georgiev
 */
@Entity
@Table(name = "delivery_types")
public class DeliveryType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
     @Column(nullable = false)
    private String type_name;

    public DeliveryType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

   
     
     
     
}
