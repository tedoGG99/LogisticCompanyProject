/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbu.logistics.serviceTest;

import com.nbu.logistics.data.*;
import com.nbu.logistics.dto.ShipmentDto;
import com.nbu.logistics.repositories.*;
import com.nbu.logistics.services.PricingService;
import com.nbu.logistics.services.ShipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShipmentServiceTest {
    @Mock
    private ShipmentRepository shipmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OfficeRepository officeRepository;
    @Mock
    private DeliveryTypeRepository deliveryTypeRepository;
    @Mock
    private ShipmentStatusRepository shipmentStatusRepository;
    @Mock
    private PricingService pricingService;

    @InjectMocks
    private ShipmentService shipmentService;

    private User employee;
    private User client;
    private Role staffRole;
    private ShipmentDto dto;
    private DeliveryType deliveryType;
    private ShipmentStatus initialStatus;

    @BeforeEach
    void setUp() {
        // ROLE
        staffRole = new Role();
        staffRole.setRole("office employee");
        staffRole.setIsStaff(true);

        // USERS
        employee = new User();
        employee.setId(1);
        employee.setRole(staffRole);

        client = new User();
        client.setId(2);
        client.setRole(new Role());

        // DTO
        dto = new ShipmentDto();
        dto.setSenderId(2);
        dto.setWeight(2.0);
        dto.setDeliveryTypeId(1);
        dto.setTargetAddress("Sofia, Bulgaria");

        // DELIVERY TYPE
        deliveryType = new DeliveryType();
        deliveryType.setRequiresOffice(false);

        // STATUS
        initialStatus = new ShipmentStatus();
        initialStatus.setId(1);
    }

    // -------------------------------------------------
    // createShipment – STAFF USER (HAPPY PATH)
    // -------------------------------------------------
    @Test
    void createShipment_staffUser_shouldSaveShipment() {
        when(userRepository.findById(2)).thenReturn(Optional.of(client));
        when(deliveryTypeRepository.findById(1)).thenReturn(Optional.of(deliveryType));
        when(shipmentStatusRepository.findFirstByIsInitialTrue())
                .thenReturn(Optional.of(initialStatus));

        when(pricingService.getBasePrice()).thenReturn(new BigDecimal("5.00"));
        when(pricingService.getWeightFactor()).thenReturn(new BigDecimal("2.00"));
        when(pricingService.getAddressSurcharge()).thenReturn(new BigDecimal("10.00"));

        shipmentService.createShipment(dto, employee);

        verify(shipmentRepository, times(1)).save(any(Shipment.class));
    }

    // -------------------------------------------------
    // findShipmentsForUser – ADMIN
    // -------------------------------------------------
    @Test
    void findShipmentsForUser_admin_shouldReturnAll() {
        Role adminRole = new Role();
        adminRole.setRole("admin");

        User admin = new User();
        admin.setRole(adminRole);

        when(shipmentRepository.findAll()).thenReturn(List.of(new Shipment()));

        List<Shipment> result = shipmentService.findShipmentsForUser(admin);

        assertEquals(1, result.size());
    }

    // -------------------------------------------------
    // updateStatus – DELIVERED
    // -------------------------------------------------
    @Test
    void updateStatus_delivered_shouldSetDateDelivered() {
        Shipment shipment = new Shipment();

        ShipmentStatus delivered = new ShipmentStatus();
        delivered.setStatusName("DELIVERED");

        when(shipmentRepository.findById(1)).thenReturn(Optional.of(shipment));
        when(shipmentStatusRepository.findByStatusName("DELIVERED"))
                .thenReturn(Optional.of(delivered));

        shipmentService.updateStatus(1, "DELIVERED", "employee");

        assertNotNull(shipment.getDateDelivered());
        verify(shipmentRepository).save(shipment);
    }

    // -------------------------------------------------
    // calculateRevenue – NULL SAFE
    // -------------------------------------------------
    @Test
    void calculateRevenue_nullFromRepo_shouldReturnZero() {
        when(shipmentRepository.calculateRevenue(any(), any()))
                .thenReturn(null);

        BigDecimal result = shipmentService.calculateRevenue(
                LocalDate.now(), LocalDate.now());

        assertEquals(BigDecimal.ZERO, result);
    }

    // -------------------------------------------------
    // findShipmentsByEmployee – NOT FOUND
    // -------------------------------------------------
    @Test
    void findShipmentsByEmployee_missingEmployee_shouldThrow() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> shipmentService.findShipmentsByEmployee(1));
    }
}
