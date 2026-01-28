package com.nbu.logistics.controllers;

import com.nbu.logistics.data.Role;
import com.nbu.logistics.data.User;
import com.nbu.logistics.dto.UserRegistrationDto;
import com.nbu.logistics.repositories.RoleRepository;
import com.nbu.logistics.services.OfficeService;
import com.nbu.logistics.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @Autowired
    private PasswordEncoder passwordEncoder;

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
    
    @PostMapping("/admin/users/{id}/delete") // or /disable
    public String disableUser(@PathVariable Integer id) {
        // Call your service to set enabled = false
        userService.deleteUser(id); 

        return "redirect:/admin/users?success=User+disabled";
    }
    

    // 4. PROCESS UPDATE
    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute User user) {
        // You need a specific update method to avoid overwriting passwords with null
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    // 1. SHOW THE FORM
    @GetMapping("/users/create")
    public String showCreateUserForm(Model model) {
        // Reuse your DTO
        model.addAttribute("user", new UserRegistrationDto());

        // Fetch offices
        model.addAttribute("offices", officeService.getAllOffices());

        // Fetch ALL roles (Do not remove Admin role here)
        model.addAttribute("roles", roleRepository.findAll());

        return "user-create"; // We need a separate template for Admin creation
    }

    @PostMapping("/users/create")
    public String createUser(@ModelAttribute UserRegistrationDto dto,
            @RequestParam Integer roleId,
            @RequestParam(required = false) Integer officeId) {

        // --- FIX IS HERE ---
        // Use the safe boolean check instead of findByUsername
        if (userService.userExists(dto.getUsername())) {
            return "redirect:/admin/users/create?error";
        }
        // -------------------

        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setEmail(dto.getEmail());
        newUser.setFirstName(dto.getFirstName());
        newUser.setLastName(dto.getLastName());
        newUser.setEnabled(true);

        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role selectedRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Role ID"));
        newUser.setRole(selectedRole);

        boolean isOfficeStaff = "office employee".equalsIgnoreCase(selectedRole.getRole())
                || "ROLE_OFFICE EMPLOYEE".equals(selectedRole.getAuthority());

        if (isOfficeStaff && officeId != null) {
            officeService.findOfficeById(officeId).ifPresent(newUser::setOffice);
        }

        userService.saveUser(newUser);

        return "redirect:/admin/users";
    }

}
