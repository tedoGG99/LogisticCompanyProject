/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbu.logistics.controllers;

import com.nbu.logistics.services.ShipmentService;
import com.nbu.logistics.services.UserService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/employee")
    public String shipmentsByEmployee(@RequestParam("employeeId") int employeeId, Model model) {
        model.addAttribute("results", shipmentService.findShipmentsByEmployee(employeeId));
        return "reports"; 
    }

    // 2. FILTER BY CLIENT
    @GetMapping("/client")
    public String shipmentsByClient(@RequestParam("clientId") int clientId, Model model) {
        model.addAttribute("results", shipmentService.findShipmentsByClient(clientId));
        return "reports";
    }

    // 3. UNRECEIVED PACKAGES
    @GetMapping("/unreceived")
    public String unreceivedShipments(Model model) {
        model.addAttribute("results", shipmentService.findUnreceivedShipments());
        return "reports";
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
