package com.nbu.logistics.controllerTest;

import com.nbu.logistics.controllers.ShipmentController;
import com.nbu.logistics.data.*;
import com.nbu.logistics.dto.ShipmentDto;
import com.nbu.logistics.services.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.util.List;


@TestConfiguration
@ComponentScan(basePackageClasses = ShipmentController.class)
public class ShipmentControllerTestConfig {

    @Bean
    public ShipmentService shipmentService() {
        return new ShipmentService() {

            @Override
            public List<Shipment> findShipmentsForUser(User user) {
                return List.of(new Shipment());
            }

            @Override
            public List<User> getAllCouriers() {
                return List.of(new User());
            }

            @Override
            public void createShipment(ShipmentDto dto, User employee) {}

            @Override
            public void updateStatus(int id, String status, String username) {}

            @Override
            public void deleteShipment(int id) {}

            @Override
            public Shipment getShipmentById(Integer id) {
                return new Shipment();
            }

            @Override
            public void assignCourier(Integer shipmentId, Integer courierId) {}
        };
    }

    @Bean
    public UserService userService() {
        return new UserService() {

            @Override
            public User findByUsername(String username) {
                Role role = new Role();
                role.setRole("ROLE_ADMIN");
                role.setIsStaff(true);

                User user = new User();
                user.setUsername(username);
                user.setRole(role);
                return user;
            }

            @Override
            public List<User> findAllClients() {
                return List.of(new User());
            }

            @Override
            public List<User> getAllUsers() {
                return List.of(new User());
            }
        };
    }
    @Bean
    public PricingService pricingService() {
        return new PricingService() {
            @Override public BigDecimal getBasePrice() { return new BigDecimal("5.00"); }
            @Override public BigDecimal getWeightFactor() { return new BigDecimal("2.00"); }
            @Override public BigDecimal getAddressSurcharge() { return new BigDecimal("3.00"); }
        };
    }

}
