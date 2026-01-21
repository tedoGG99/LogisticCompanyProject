/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nbu.logistics.repositories;

import com.nbu.logistics.data.DeliveryType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author tedi
 */
public interface DeliveryTypeRepository extends JpaRepository<DeliveryType, Long> {
    
    DeliveryType findByTypeName(String type);
    
    
}
