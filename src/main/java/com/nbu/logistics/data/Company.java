package com.company.logistics.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @Column(name = "CompanyID")
    private Integer id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "VAT_Number", nullable = false)
    private Integer vatNumber;

    @Column(name = "MainAddress", nullable = false)
    private String mainAddress;
}