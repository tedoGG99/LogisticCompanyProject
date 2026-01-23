
package com.nbu.logistics.controllers;

import com.nbu.logistics.data.User;
import com.nbu.logistics.dto.ShipmentDto;
import com.nbu.logistics.services.OfficeService;
import com.nbu.logistics.services.ShipmentService;
import com.nbu.logistics.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/shipments")
public class ShipmentController {
    
    @Autowired 
    private ShipmentService shipmentService;
    @Autowired 
    private OfficeService officeService;
    @Autowired 
    private UserService userService;
    
    // 1. DASHBOARD: List shipments based on who is logged in
    @GetMapping
    public String listShipments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        
        // Service logic decides: Employee gets ALL, Client gets THEIRS
        model.addAttribute("shipments", shipmentService.findShipmentsForUser(currentUser));
        
        return "shipment-list"; // HTML Template name
    }
    
    // 2. CREATE FORM: Only for Employees (Secured by SecurityConfig)
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("shipmentDto", new ShipmentDto());
        
        // Populate dropdowns
        model.addAttribute("offices", officeService.getAllOffices());
        model.addAttribute("clients", userService.findAllClients()); // For "Sender" dropdown
        
        return "shipment-create";
    }
    
    // 3. PROCESS CREATE
    @PostMapping("/create")
    public String createShipment(@ModelAttribute("shipmentDto") ShipmentDto shipmentDto,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        User employee = userService.findByUsername(userDetails.getUsername());
        
        shipmentService.createShipment(shipmentDto, employee);
        
        return "redirect:/shipments";
    }

    // 4. CHANGE STATUS (e.g., Receive/Deliver)
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable int id, @RequestParam("status") String newStatus) {
        shipmentService.updateStatus(id, newStatus);
        return "redirect:/shipments";
    }
    
    // 5. DELETE SHIPMENT (Optional, but usually good to have)
    @PostMapping("/{id}/delete")
    public String deleteShipment(@PathVariable int id) {
        shipmentService.deleteShipment(id);
        return "redirect:/shipments";
    }
    
}
