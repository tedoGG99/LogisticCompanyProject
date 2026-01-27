package com.nbu.logistics.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ShipmentDto {

    private Integer id;

    // ==========================
    // SENDER
    // ==========================
    @NotNull(message = "Sender ID is required")
    @Min(value = 1, message = "Sender ID must be valid")
    private Integer senderId;

    @Email(message = "Sender email must be valid")
    private String senderEmail;

    // ==========================
    // RECEIVER
    // ==========================
    // Optional registered receiver
    private String receiverUsername;

    // Manual receiver username (Guest)
    private String receiverManualUsername;

    @Size(min = 2, message = "Receiver name must be at least 2 characters")
    private String receiverName;

    private String receiverPhone;

    @NotBlank(message = "Receiver email is required")
    @Email(message = "Receiver email must be valid")
    private String receiverEmail;

    // ==========================
    // SHIPMENT DATA
    // ==========================
    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be greater than zero")
    private Double weight;

    // ==========================
    // DATABASE IDs
    // ==========================
    @NotNull(message = "Delivery Type ID is required")
    private Integer deliveryTypeId;

    private Integer statusId;

    // ==========================
    // CONDITIONAL FIELDS
    // ==========================
    private Integer targetOfficeId;
    private String targetAddress;

    // ==========================================
    // CONSTRUCTORS
    // ==========================================
    public ShipmentDto() {
    }

    public ShipmentDto(Integer id,
                       Integer senderId,
                       String senderEmail,
                       String receiverUsername,
                       String receiverManualUsername,
                       String receiverName,
                       String receiverPhone,
                       String receiverEmail,
                       Double weight,
                       Integer deliveryTypeId,
                       Integer statusId,
                       Integer targetOfficeId,
                       String targetAddress) {
        this.id = id;
        this.senderId = senderId;
        this.senderEmail = senderEmail;
        this.receiverUsername = receiverUsername;
        this.receiverManualUsername = receiverManualUsername;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverEmail = receiverEmail;
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

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getReceiverManualUsername() {
        return receiverManualUsername;
    }

    public void setReceiverManualUsername(String receiverManualUsername) {
        this.receiverManualUsername = receiverManualUsername;
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

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
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
