package com.company.logistics.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "courier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Courier {

    @Id
    @Column(name = "employee_user_UserId")
    private Integer employeeUserId;

    @Column(name = "FirstName", nullable = false)
    private String firstName;

    @Column(name = "LastName", nullable = false)
    private String lastName;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Shipment_ID", nullable = false)
    private Integer shipmentId;

    @Column(name = "office_OfficeID", nullable = false)
    private Integer officeId;
}