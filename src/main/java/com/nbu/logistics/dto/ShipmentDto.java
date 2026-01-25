/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbu.logistics.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 *
 * @author tedi
 */
public class ShipmentDto {
    
    private Integer id;

    @NotNull(message = "Sender ID is required")
    @Min(value = 1, message = "Sender ID must be valid")
    private Integer senderId;

    // --- Receiver Info ---
    // Note: Cross-field validation (e.g. "if receiverName is null, username must be set")
    // is usually handled in the Service layer, not by simple annotations.
    private String receiverUsername; 
    
    @Size(min = 2, message = "Receiver name must be at least 2 characters")
    private String receiverName;     
    
    private String receiverPhone;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be greater than zero")
    private Double weight;

    // --- Database IDs (Integration) ---

    @NotNull(message = "Delivery Type ID is required")
    private Integer deliveryTypeId; 

    // Status is usually null on creation (defaults to CREATED), 
    // but useful for updates.
    private Integer statusId;

    // --- Conditional Fields ---
    private Integer targetOfficeId; 
    private String targetAddress;

    // ==========================================
    // CONSTRUCTORS
    // ==========================================

    // 1. No-Args Constructor (Required for JSON/Jackson)
    public ShipmentDto() {
    }

    // 2. All-Args Constructor (Optional, useful for tests)
    public ShipmentDto(Integer id, Integer senderId, String receiverUsername, 
                       String receiverName, String receiverPhone, Double weight, 
                       Integer deliveryTypeId, Integer statusId, 
                       Integer targetOfficeId, String targetAddress) {
        this.id = id;
        this.senderId = senderId;
        this.receiverUsername = receiverUsername;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.weight = weight;
        this.deliveryTypeId = deliveryTypeId;
        this.statusId = statusId;
        this.targetOfficeId = targetOfficeId;
        this.targetAddress = targetAddress;
    }

    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getDeliveryTypeId() {
        return deliveryTypeId;
    }

    public void setDeliveryTypeId(Integer deliveryTypeId) {
        this.deliveryTypeId = deliveryTypeId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getTargetOfficeId() {
        return targetOfficeId;
    }

    public void setTargetOfficeId(Integer targetOfficeId) {
        this.targetOfficeId = targetOfficeId;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }
    
}
