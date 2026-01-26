
package com.nbu.logistics.controllers;

import com.nbu.logistics.data.Role;
import com.nbu.logistics.data.User;
import com.nbu.logistics.dto.UserRegistrationDto;
import com.nbu.logistics.repositories.UserRepository;
import com.nbu.logistics.services.OfficeService;
import com.nbu.logistics.services.UserService;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class AuthController {
    private final UserService userService; 
    private final PasswordEncoder passwordEncoder;
    private final OfficeService officeService;

    // Constructor Injection
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, OfficeService officeService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.officeService = officeService;
    }
    
    @GetMapping("/")
    public String homePage(Model model, Principal principal) {
        // 'Principal' holds the logged-in user's info
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        return "home"; // This looks for home.html
    }

    // --- 1. Login UI ---
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Serves login.html
    }

    // --- 2. Register UI ---
    // 2. Show Registration Form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // "user" is the key we will use in the HTML th:object
        model.addAttribute("user", new UserRegistrationDto());
        
        // We need the list of offices for the dropdown
        model.addAttribute("offices", officeService.getAllOffices());
        List<Role> roles = userService.getAllRoles();
        Role role = userService.getAdminRole();
        if(role != null){
            roles.remove(role);
        }
        model.addAttribute("roles", roles);
        return "register";
    }

    // 3. Process Registration Data
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto registrationDto, 
                               Model model) {
        try {
            // CALL THE SERVICE TO SAVE TO DATABASE
            userService.registerUser(registrationDto);
            
            // Success: Redirect to login
            return "redirect:/login?success";
            
        } catch (RuntimeException e) {
            // Failure: Reload page with error message
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", registrationDto); // Keep what they typed
            model.addAttribute("offices", officeService.getAllOffices());
            return "register";
        }
    }
    
    
    
}
