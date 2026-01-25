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
@Table(name = "roles")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Stores values like: "CLIENT", "OFFICE_EMPLOYEE", "COURIER"
    @Column(nullable = false, unique = true)
    private String role;
    
    @Column(name = "is_staff")
    private Boolean isStaff;

    public Role() {
    }

    public Role(Integer id, String role) {
        this.id = id;
        this.role = role;
    }
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(Boolean isStaff) {
        this.isStaff = isStaff;
    }
    
    
    
    
}
