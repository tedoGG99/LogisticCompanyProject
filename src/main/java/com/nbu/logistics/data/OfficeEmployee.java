package com.company.logistics.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "office_employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfficeEmployee {

    @Id
    @Column(name = "employee_user_UserId")
    private Integer employeeUserId;

    @Column(name = "office_OfficeID", nullable = false)
    private Integer officeId;
}
