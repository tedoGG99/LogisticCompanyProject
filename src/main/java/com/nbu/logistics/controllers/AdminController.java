
package com.nbu.logistics.controllers;

import com.nbu.logistics.data.User;
import com.nbu.logistics.repositories.RoleRepository;
import com.nbu.logistics.services.OfficeService;
import com.nbu.logistics.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    
    @Autowired
    private OfficeService officeService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleRepository roleRepository; 
    
    
    // 1. LIST OFFICES
    @GetMapping("/offices")
    public String listOffices(Model model) {
        model.addAttribute("offices", officeService.getAllOffices());
        return "admin-offices";
    }

    // 2. LIST USERS
    @GetMapping("/users")
    public String listUsers(Model model) {
        // Ensure your UserService has a getAllUsers() method
        model.addAttribute("users", userService.getAllUsers());
        return "admin-users";
    }
    
    // 3. SHOW EDIT FORM
    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Integer id, Model model) {
        // Find user (you might need to add findById to UserService)
        User user = userService.getUserById(id);
        
        if (user == null) {
            return "redirect:/admin/users"; // Handle invalid ID
        }

        model.addAttribute("user", user);
        
        // Load options for the dropdowns
        model.addAttribute("allRoles", roleRepository.findAll());
        model.addAttribute("allOffices", officeService.getAllOffices());
        
        return "user-edit";
    }
    
    // 4. PROCESS UPDATE
    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute User user) {
        // You need a specific update method to avoid overwriting passwords with null
        userService.updateUser(user); 
        return "redirect:/admin/users";
    }
    
}
