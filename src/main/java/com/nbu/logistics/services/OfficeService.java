
package com.nbu.logistics.services;

import com.nbu.logistics.data.Office;
import com.nbu.logistics.repositories.OfficeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
    
    public void createOffice(Office office) {
        if (office == null) {
            throw new IllegalArgumentException("Office cannot be null");
        }
        // This saves the new office to the database
        officeRepository.save(office);
    }
    
    public Optional<Office> findOfficeById(Integer id){
        return officeRepository.findById(id);
        
    }
    
}
