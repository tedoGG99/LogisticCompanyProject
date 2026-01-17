package com.nbu.logistics.controller;

import com.company.logistics.entity.Client;
import com.company.logistics.exception.ResourceNotFoundException;
import com.company.logistics.service.ClientService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/{id}")
    public ResponseEntity<Client> getById(@PathVariable Integer id) {

        Client client = clientService.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found"));

        return ResponseEntity.ok(client);
    }
}     // За сега работи с Entity, но ще го направим да работи с DTO като направим логиката