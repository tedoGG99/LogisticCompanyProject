/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbu.logistics.data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
/**
 *
 * @author tedi
 */


@Entity
@Table(name = "users")
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
            
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;
    
    // Soft Delete field (as discussed)
    @Column(nullable = false)
    private boolean enabled = true;

    // --- CHANGED: ROLE RELATIONSHIP ---
    
    // Many Users can share the same Role (e.g., many Clients)
    @ManyToOne(fetch = FetchType.EAGER) // Eager fetch is useful for Roles to avoid login errors
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // --- OFFICE RELATIONSHIP ---
    
    // Mandatory for OFFICE_EMPLOYEE, null for others
    @ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;

    // --- SHIPMENT RELATIONSHIPS ---

    @OneToMany(mappedBy = "sender")
    private List<Shipment> sentShipments;

    @OneToMany(mappedBy = "receiver")
    private List<Shipment> receivedShipments;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public List<Shipment> getSentShipments() {
        return sentShipments;
    }

    public void setSentShipments(List<Shipment> sentShipments) {
        this.sentShipments = sentShipments;
    }

    public List<Shipment> getReceivedShipments() {
        return receivedShipments;
    }

    public void setReceivedShipments(List<Shipment> receivedShipments) {
        this.receivedShipments = receivedShipments;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // We append "ROLE_" here and uppercase it so Spring Security works normally
        // e.g., "client" becomes "ROLE_CLIENT"
        return Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + role.getRole().toUpperCase())
        );
    }

    
    
    
}
