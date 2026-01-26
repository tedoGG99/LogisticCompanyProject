
package com.nbu.logistics.controllers;

import com.nbu.logistics.data.Shipment;
import com.nbu.logistics.data.ShipmentStatus;
import com.nbu.logistics.data.User;
import com.nbu.logistics.dto.ShipmentDto;
import com.nbu.logistics.repositories.DeliveryTypeRepository;
import com.nbu.logistics.repositories.ShipmentStatusRepository;
import com.nbu.logistics.services.OfficeService;
import com.nbu.logistics.services.PricingService;
import com.nbu.logistics.services.ShipmentService;
import com.nbu.logistics.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/shipments")
public class ShipmentController {
    
    @Autowired 
    private ShipmentService shipmentService;
    @Autowired 
    private ShipmentStatusRepository shipmentStatusRepository;
    @Autowired 
    private OfficeService officeService;
    @Autowired 
    private UserService userService;
    @Autowired
    private DeliveryTypeRepository deliveryTypeRepository;
    
    @Autowired
    private PricingService pricingService;
    
    // 1. DASHBOARD: List shipments based on who is logged in
    @GetMapping
    public String listShipments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User currentUser = userService.findByUsername(userDetails.getUsername());

        // 1. DATA: The Shipments
        model.addAttribute("shipments", shipmentService.findShipmentsForUser(currentUser));

        // 2. DYNAMIC PERMISSIONS (Instead of sec:authorize with hardcoded strings)
        // We check the 'is_staff' flag from the Role table in DB
        boolean isStaff = Boolean.TRUE.equals(currentUser.getRole().getIsStaff());
        model.addAttribute("isStaff", isStaff);
        model.addAttribute("couriers", shipmentService.getAllCouriers());
        // 3. DYNAMIC STATUS IDs (For the buttons)
        // We fetch the actual ID of the 'DELIVERED' status from the DB to avoid hardcoding "4" or "5"
        // Assuming you added 'is_final' column, or we look up by a convention
        ShipmentStatus deliveredStatus = shipmentStatusRepository.findFirstByIsFinalTrue()
            .orElse(null); // or handle error

        // If you don't have is_final, you might need to look up by name safely or add the column
        // For now, let's assume you added 'is_final' to the DB as discussed before.
        if (deliveredStatus != null) {
            model.addAttribute("deliveredStatusId", deliveredStatus.getId());
        }

        // Pass the current user's role ID or name for other logic if needed
        model.addAttribute("currentUserRole", currentUser.getRole().getRole());

        return "shipment-list";
    }
    
    
    // 2. CREATE FORM: Only for Employees (Secured by SecurityConfig)
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("shipmentDto", new ShipmentDto());

        // 1. DROPDOWNS DATA
        model.addAttribute("offices", officeService.getAllOffices());

        // 2. We need Delivery Types to know which one requires an office
        model.addAttribute("deliveryTypes", deliveryTypeRepository.findAll()); 

        // 3. Optional: Only show clients list if the logged-in user is an Employee
        // (You can handle this check in the HTML too via Thymeleaf)
        model.addAttribute("clients", userService.findAllClients());
        model.addAttribute("allUsers", userService.getAllUsers());
        
        // PASS PRICING CONFIG TO HTML so JS can use it
        model.addAttribute("basePrice", pricingService.getBasePrice());
        model.addAttribute("weightFactor", pricingService.getWeightFactor());
        model.addAttribute("surcharge", pricingService.getAddressSurcharge());

        return "shipment-create"; // This looks for shipment-create.html in templates
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
    public String updateStatus(@PathVariable int id, 
                               @RequestParam("status") String status, // <--- Accepts "SENT", "DELIVERED"
                               @AuthenticationPrincipal UserDetails userDetails) {

        // Pass the Username so we can log WHO updated it
        shipmentService.updateStatus(id, status, userDetails.getUsername());

        return "redirect:/shipments";
    }
    
    // 5. DELETE SHIPMENT (Optional, but usually good to have)
    @PostMapping("/{id}/delete")
    public String deleteShipment(@PathVariable int id) {
        shipmentService.deleteShipment(id);
        return "redirect:/shipments";
    }
    
    @GetMapping("/whoami")
    @ResponseBody
    public String whoAmI(@AuthenticationPrincipal UserDetails userDetails) {
        // This will print exactly what Spring Security sees
        return "User: " + userDetails.getUsername() + 
               " | Authorities: " + userDetails.getAuthorities();
    }
    
    // Display Shipment Details
    @GetMapping("/{id}")
    public String showShipmentDetails(@PathVariable Integer id, Model model) {
        Shipment shipment = shipmentService.getShipmentById(id);
        
        // Safety check: if ID doesn't exist, go back to list
        if (shipment == null) {
            return "redirect:/shipments";
        }
        
        model.addAttribute("shipment", shipment);
        return "shipment-details"; // This looks for shipment-details.html
    }
    
    @PostMapping("/{id}/assign-courier")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OFFICE EMPLOYEE')")
    public String assignCourier(@PathVariable Integer id, @RequestParam Integer courierId) {

        shipmentService.assignCourier(id, courierId);

        return "redirect:/shipments";
    }
    
    
    
    
    
}
