package com.nbu.logistics.controllerTest;

import com.nbu.logistics.controllers.ReportController;
import com.nbu.logistics.data.Shipment;
import com.nbu.logistics.data.User;
import com.nbu.logistics.services.ShipmentService;
import com.nbu.logistics.services.UserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@TestConfiguration
@ComponentScan(basePackageClasses = ReportController.class)
public class ReportControllerTestConfig {
    @Bean
    public ShipmentService shipmentService() {
        return new ShipmentService() {

            @Override
            public List<Shipment> findShipmentsByEmployee(int employeeId) {
                Shipment s = new Shipment();
                s.setId(1);
                s.setPrice(new BigDecimal("100.00"));
                s.setReceiverName("Receiver Test");
                return List.of(s);
            }

            @Override
            public List<Shipment> findShipmentsByClient(int clientId) {
                return List.of();
            }

            @Override
            public List<Shipment> findUnreceivedShipments() {
                return List.of();
            }

            @Override
            public BigDecimal calculateRevenue(LocalDate startDate, LocalDate endDate) {
                return new BigDecimal("999.99");
            }
        };
    }

    @Bean
    public UserService userService() {
//        return new UserService() {
//
//            
//        };
        return null;
//        return new UserService() {
//
//            
//        };
    }
}
