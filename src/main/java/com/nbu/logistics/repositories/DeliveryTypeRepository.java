
package com.nbu.logistics.repositories;

import com.nbu.logistics.data.DeliveryType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DeliveryTypeRepository extends JpaRepository<DeliveryType, Integer> {
    
    Optional<DeliveryType> findByTypeName(String typeName);
    
    
}
