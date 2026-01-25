package com.company.logistics.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @Column(name = "user_UserId")
    private Integer userId;

    @Column(name = "FirstName", nullable = false)
    private String firstName;

    @Column(name = "LastName", nullable = false)
    private String lastName;

    @Column(name = "Shipment_Sent_ID")
    private Integer shipmentSentId;

    @Column(name = "Shipment_Recieve_ID1")
    private Integer shipmentReceivedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_UserId", insertable = false, updatable = false)
    private User user;
}
