
package com.nbu.logistics.repositories;

import com.nbu.logistics.data.Office;
import com.nbu.logistics.data.Shipment;
import com.nbu.logistics.data.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ShipmentRepository extends JpaRepository<Shipment, Integer>{
    
    Optional<Shipment> findById(int id );
    
    List<Shipment> findByOffice(Office office);
    List<Shipment> findBySender(User sender);
    
    List<Shipment> findByReceiver(User receiver);
    
    List<Shipment> findByCourier(User user);
    
    @Query("SELECT s FROM Shipment s WHERE s.status.statusName NOT IN ('RECEIVED', 'DELIVERED')")
    List<Shipment> findAllUnreceivedShipments();
    
    @Query("SELECT SUM(s.price) FROM Shipment s WHERE s.dateRegistered BETWEEN :startDate AND :endDate")
    BigDecimal calculateRevenue(LocalDateTime startDate, LocalDateTime endDate);
    
    @Override
    List<Shipment> findAll();
}
