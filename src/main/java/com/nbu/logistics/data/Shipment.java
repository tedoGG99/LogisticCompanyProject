package com.nbu.logistics.data;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String trackingNumber;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private BigDecimal price;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dateRegistered;

    private LocalDateTime dateDelivered;

    // ==========================================================
    // RELATIONS
    // ==========================================================
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    // ==========================================================
    // RECEIVER DETAILS (always stored)
    // ==========================================================
    private String receiverName;

    private String receiverPhone;

    // NEW: receiver email always stored (guest OR registered)
    @Column(name = "receiver_email")
    private String receiverEmail;

    // NEW: for guest/manual username
    @Column(name = "receiver_username")
    private String receiverUsername;

    // ==========================================================
    // SENDER CONTACT EMAIL (custom/override)
    // ==========================================================
    @Column(name = "sender_email")
    private String senderEmail;

    // ==========================================================
    // DELIVERY LOGIC
    // ==========================================================
    @ManyToOne
    @JoinColumn(name = "delivery_type_id", nullable = false)
    private DeliveryType deliveryTypeId;

    @ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;

    private String deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private ShipmentStatus status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private User courier;

    // ==========================================================
    // CONSTRUCTORS
    // ==========================================================
    public Shipment() {
    }

    public Shipment(Integer id, String trackingNumber, Double weight, BigDecimal price,
                    LocalDateTime dateRegistered, LocalDateTime dateDelivered,
                    User sender, User receiver,
                    String receiverName, String receiverPhone,
                    String receiverEmail, String receiverUsername,
                    String senderEmail,
                    DeliveryType deliveryTypeId, Office office, String deliveryAddress,
                    ShipmentStatus status, User employee, User courier) {

        this.id = id;
        this.trackingNumber = trackingNumber;
        this.weight = weight;
        this.price = price;
        this.dateRegistered = dateRegistered;
        this.dateDelivered = dateDelivered;

        this.sender = sender;
        this.receiver = receiver;

        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverEmail = receiverEmail;
        this.receiverUsername = receiverUsername;

        this.senderEmail = senderEmail;

        this.deliveryTypeId = deliveryTypeId;
        this.office = office;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.employee = employee;
        this.courier = courier;
    }

    // ==========================================================
    // GETTERS / SETTERS
    // ==========================================================
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDateTime dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public LocalDateTime getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(LocalDateTime dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
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

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public DeliveryType getDeliveryTypeId() {
        return deliveryTypeId;
    }

    public void setDeliveryTypeId(DeliveryType deliveryTypeId) {
        this.deliveryTypeId = deliveryTypeId;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public User getCourier() {
        return courier;
    }

    public void setCourier(User courier) {
        this.courier = courier;
    }

    // ==========================================================
    // HELPER
    // ==========================================================
    public String getSenderName() {
        if (this.sender != null) {
            return this.sender.getUsername();
        }
        return "Unknown Sender";
    }
}
