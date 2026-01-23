/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbu.logistics.services;

import com.nbu.logistics.data.Office;
import com.nbu.logistics.repositories.OfficeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tedi
 */
@Service
public class OfficeService {
    @Autowired
    private OfficeRepository officeRepository;

    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }
    
    public Office getOfficeById(int id) {
        return officeRepository.findById(id).orElseThrow(() -> new RuntimeException("Office not found"));
    }
}
