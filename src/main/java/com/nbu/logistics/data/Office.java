package com.company.logistics.entity;


import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "office")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Office {

    @Id
    @Column(name = "OfficeID")
    private Integer id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Company_CompanyID", nullable = false)
    private Integer companyId;

    @Column(name = "Address_AddressID", nullable = false)
    private Integer addressId;
}
