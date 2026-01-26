
package com.nbu.logistics.config;

import com.nbu.logistics.data.DeliveryType;
import com.nbu.logistics.data.Role;
import com.nbu.logistics.data.ShipmentStatus;
import com.nbu.logistics.repositories.DeliveryTypeRepository;
import com.nbu.logistics.repositories.RoleRepository;
import com.nbu.logistics.repositories.ShipmentStatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final ShipmentStatusRepository shipmentStatusRepository;
    private final DeliveryTypeRepository deliveryTypeRepository;

    public DataSeeder(RoleRepository roleRepository, 
                      ShipmentStatusRepository shipmentStatusRepository, 
                      DeliveryTypeRepository deliveryTypeRepository) {
        this.roleRepository = roleRepository;
        this.shipmentStatusRepository = shipmentStatusRepository;
        this.deliveryTypeRepository = deliveryTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        
        // 1. SEED ROLES
        // Names must match exactly what UserService looks for ("client", "office_employee", etc.)
        seedRole("admin");
        seedRole("client");
        seedRole("office_employee");
        seedRole("courier");

        // 2. SEED SHIPMENT STATUSES
        // Names must match exactly what ShipmentService looks for
        seedStatus("REGISTERED");
        seedStatus("SENT");
        seedStatus("RECEIVED");
        seedStatus("DELIVERED");

        // 3. SEED DELIVERY TYPES
        seedDeliveryType("TO_OFFICE");
        seedDeliveryType("TO_ADDRESS");
    }

    // --- HELPER METHODS ---

    private void seedRole(String authority) {
        // Check if exists to avoid duplicates
        if (roleRepository.findByRole(authority) == null) {
            Role role = new Role();
            role.setRole(authority); // Ensure your Role entity has this setter
            roleRepository.save(role);
        }
    }

    private void seedStatus(String statusName) {
        // IMPORTANT: Ensure this method name matches your Repository fix (findByStatusName or findByStatus_name)
        // I am assuming you fixed the entity to use CamelCase "statusName"
        if (shipmentStatusRepository.findByStatusName(statusName) == null) {
            ShipmentStatus status = new ShipmentStatus();
            
            // NOTE: Check your ShipmentStatus.java. 
            // If field is 'status_name', setter might be 'setStatus_name'
            // If field is 'statusName', setter is 'setStatusName'
            status.setStatusName(statusName); 
            
            shipmentStatusRepository.save(status);
        }
    }

    private void seedDeliveryType(String typeName) {
        // IMPORTANT: This uses the fix we just discussed (findByTypeName)
        if (deliveryTypeRepository.findByTypeName(typeName) == null) {
            DeliveryType type = new DeliveryType();
            
            // NOTE: Check your DeliveryType.java.
            // Ensure you renamed the field to 'typeName' so this setter works
            type.setTypeName(typeName);
            
            deliveryTypeRepository.save(type);
        }
    }
}
