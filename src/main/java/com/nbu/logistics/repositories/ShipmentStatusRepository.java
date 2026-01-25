/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nbu.logistics.repositories;

import com.nbu.logistics.data.Shipment;
import com.nbu.logistics.data.ShipmentStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author tedi
 */

public interface ShipmentStatusRepository extends JpaRepository<ShipmentStatus, Integer> { 
          
    Optional<ShipmentStatus> findByStatusName(String statusName);
    
    Optional<ShipmentStatus> findFirstByIsInitialTrue();
}
