
package com.nbu.logistics.repositories;

import com.nbu.logistics.data.ShipmentStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ShipmentStatusRepository extends JpaRepository<ShipmentStatus, Integer> { 
          
    Optional<ShipmentStatus> findByStatusName(String statusName);
    
    Optional<ShipmentStatus> findFirstByIsInitialTrue();
    Optional<ShipmentStatus> findFirstByIsFinalTrue();
    
    
    
    
}
