
package com.nbu.logistics.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "shipment_statuses")
public class ShipmentStatus {
//    REGISTERED, // Registered by employee
//    SENT,       // On the way
//    RECEIVED,   // Arrived at destination office
//    DELIVERED   // Picked up by client

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "status_name", nullable = false, unique = true)
    private String statusName;
    
    @Column(name = "is_initial")
    private Boolean isInitial;

    // Optional: marks which status means "finished" (e.g. DELIVERED = true)
    @Column(name = "is_final")
    private Boolean isFinal;

    public ShipmentStatus() {
    }

    public ShipmentStatus(int id, String status) {
        this.id = id;
        this.statusName = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Boolean getIsInitial() {
        return isInitial;
    }

    public void setIsInitial(Boolean isInitial) {
        this.isInitial = isInitial;
    }

    public Boolean getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(Boolean isFinal) {
        this.isFinal = isFinal;
    }
    
    

    

   
    
    

}
