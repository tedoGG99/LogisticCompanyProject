
package com.nbu.logistics.controllers;

import com.nbu.logistics.data.Office;
import com.nbu.logistics.services.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/offices")
public class OfficeController {
    @Autowired
    private OfficeService officeService;

    // 1. Show the Form
    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("office", new Office());
        return "office-create";
    }

    // 2. Process the Save
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String createOffice(@ModelAttribute Office office) {
        officeService.createOffice(office); // You need this method in your Service
        return "redirect:/shipments"; // Redirect to dashboard after success
    }
}
