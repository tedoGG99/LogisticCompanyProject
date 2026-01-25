/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbu.logistics.controllers;

import com.nbu.logistics.data.Shipment;
import com.nbu.logistics.services.ShipmentService;
import com.nbu.logistics.services.UserService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reports")
public class ReportController {
    @Autowired 
    private UserService userService;
    @Autowired 
    private ShipmentService shipmentService;
    
    // MAIN REPORT PAGE
    @GetMapping
    public String showReportsPage(Model model) {
        // Load dropdowns for filtering
        model.addAttribute("employees", userService.findAllEmployees());
        model.addAttribute("clients", userService.findAllClients());
        return "reports"; // corresponds to reports.html
    }

    // 1. FILTER BY EMPLOYEE
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Shipment>> getShipmentsByEmployee(@PathVariable int employeeId) {
        List<Shipment> shipments = shipmentService.findShipmentsByEmployee(employeeId);
        
        if (shipments.isEmpty()) {
            return ResponseEntity.noContent().build(); // Returns 204 if list is empty
        }
        return ResponseEntity.ok(shipments);
    }

    // 2. FILTER BY CLIENT
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Shipment>> getShipmentsByClient(@PathVariable int clientId) {
        List<Shipment> shipments = shipmentService.findShipmentsByClient(clientId);

        if (shipments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(shipments);
    }

    // 3. UNRECEIVED PACKAGES
    @GetMapping("/unreceived")
    public ResponseEntity<List<Shipment>> getUnreceivedShipments() {
        List<Shipment> shipments = shipmentService.findUnreceivedShipments();
        
        if (shipments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(shipments);
    }

    // 4. REVENUE REPORT
    @GetMapping("/revenue")
    public String revenueReport(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                Model model) {
        
        BigDecimal revenue = shipmentService.calculateRevenue(startDate, endDate);
        model.addAttribute("revenue", revenue);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        return "reports";
    }
    
}
