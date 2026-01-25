package com.nbu.logistics.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 *
 * @author tedi
 */
public class ShipmentDto {

    @PositiveOrZero(message = "id must be 0 or positive")
    private int id;

    @Positive(message = "senderId must be a positive number")
    private int senderId;           // The client sending the package

    @Size(min = 3, max = 255, message = "receiverUsername must be between 3 and 255 characters")
    private String receiverUsername; // If receiver is a registered user

    @Size(min = 2, max = 100, message = "receiverName must be between 2 and 100 characters")
    private String receiverName;     // If guest

    @Pattern(
            regexp = "^[0-9+\\-\\s]{6,20}$",
            message = "receiverPhone must be a valid phone number (6-20 chars)"
    )
    private String receiverPhone;    // If guest

    @NotNull(message = "weight is required")
    @DecimalMin(value = "0.01", message = "weight must be > 0")
    private Double weight;

    @NotBlank(message = "deliveryType is required")
    @Pattern(
            regexp = "^(TO_OFFICE|TO_ADDRESS)$",
            message = "deliveryType must be TO_OFFICE or TO_ADDRESS"
    )
    private String deliveryType;     // "TO_OFFICE" or "TO_ADDRESS"

    @PositiveOrZero(message = "targetOfficeId must be 0 or positive")
    private int targetOfficeId;     // If TO_OFFICE

    @Size(min = 5, max = 255, message = "targetAddress must be between 5 and 255 characters")
    private String targetAddress;    // If TO_ADDRESS

    @NotBlank(message = "status is required")
    @Size(min = 2, max = 50, message = "status must be between 2 and 50 characters")
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
