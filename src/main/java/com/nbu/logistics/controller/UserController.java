package com.nbu.logistics.controller;

import com.company.logistics.entity.User;
import com.company.logistics.exception.ResourceNotFoundException;
import com.company.logistics.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/by-username/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable String username) {

        User user = userService.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok(user);
    }
}             // За сега работи с Entity, но ще го направим да работи с DTO като направим логиката