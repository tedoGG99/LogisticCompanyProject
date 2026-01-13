/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbu.logistics.controllers;

import com.nbu.logistics.data.User;
import com.nbu.logistics.repositories.UserRepository;
import java.security.Principal;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author tedi
 */
@Controller
public class AuthController {
    private final UserRepository userRepository; 
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    @GetMapping("/register")
    public String registerPage() {
        return "register"; // Serves register.html
    }

    // --- 3. Handle Registration (Works for UI form AND cURL) ---
    @PostMapping("/register")
    public String registerUser(
            @RequestParam("username") String username, 
            @RequestParam("password") String password,
            @RequestParam("email") String email) {
        
        // Check if user exists (optional safety)
        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/register?error";
        }

        // Create and Save User
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password)); // Hash it!
        newUser.setEmail(email);
        userRepository.save(newUser);

        // Redirect to login page after success
        return "redirect:/login?success";
    }
    
    
    
}
