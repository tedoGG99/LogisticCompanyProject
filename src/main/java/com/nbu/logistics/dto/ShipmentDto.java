/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbu.logistics.dto;

/**
 *
 * @author tedi
 */
public class ShipmentDto {
    
    private int id;
    private int senderId;           // The client sending the package
    private String receiverUsername; // If receiver is a registered user
    private String receiverName;     // If guest
    private String receiverPhone;    // If guest
    
    private Double weight;
    private String deliveryType;     // "TO_OFFICE" or "TO_ADDRESS"
    private int targetOfficeId;     // If TO_OFFICE
    private String targetAddress;    // If TO_ADDRESS
    
    private String status;           // Status update
    
    
    
    // Default No-Args Constructor
    public ShipmentDto() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
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

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public int getTargetOfficeId() {
        return targetOfficeId;
    }

    public void setTargetOfficeId(int targetOfficeId) {
        this.targetOfficeId = targetOfficeId;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
