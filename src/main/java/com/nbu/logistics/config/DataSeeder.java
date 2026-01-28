
package com.nbu.logistics.config;

import com.nbu.logistics.data.DeliveryType;
import com.nbu.logistics.data.Role;
import com.nbu.logistics.data.ShipmentStatus;
import com.nbu.logistics.data.User;
import com.nbu.logistics.repositories.DeliveryTypeRepository;
import com.nbu.logistics.repositories.RoleRepository;
import com.nbu.logistics.repositories.ShipmentStatusRepository;
import com.nbu.logistics.repositories.UserRepository;
import java.util.Optional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final ShipmentStatusRepository shipmentStatusRepository;
    private final DeliveryTypeRepository deliveryTypeRepository;
    
    // NEW: Add these two
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(RoleRepository roleRepository, 
                      ShipmentStatusRepository shipmentStatusRepository, 
                      DeliveryTypeRepository deliveryTypeRepository,
                      UserRepository userRepository,      // <--- Inject this
                      PasswordEncoder passwordEncoder) {  // <--- Inject this
        this.roleRepository = roleRepository;
        this.shipmentStatusRepository = shipmentStatusRepository;
        this.deliveryTypeRepository = deliveryTypeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("---------------------------------------------");
        System.out.println("⚡⚡⚡ DATA SEEDER IS RUNNING ⚡⚡⚡");
        System.out.println("---------------------------------------------");
            // 1. SEED ROLES (Now with isStaff flag!)
        seedRole("admin", true);
        seedRole("client", false);
        seedRole("office employee", true);
        seedRole("courier", true);

        // 2. SEED SHIPMENT STATUSES (Now with initial/final flags!)
        seedStatus("REGISTERED", true, false);
        seedStatus("SENT", false, false);
        seedStatus("DELIVERED", false, true);

        // 3. SEED DELIVERY TYPES (Now with requiresOffice flag!)
        seedDeliveryType("TO_OFFICE", true);
        seedDeliveryType("TO_ADDRESS", false);
        
        // 4. (Optional) Create a default Admin User if none exists?
        seedAdminUser();
    }

    // --- HELPER METHODS ---

    // 1. Fix seedRole
    private void seedRole(String authority, boolean isStaff) {
        // .findByRole might return Optional, usually better to use .isPresent() check
        // If your repo returns 'Role', keep '!= null'. 
        // If it returns 'Optional<Role>', you MUST change it.

        // SAFE FIX: Use 'findByRole' and check if it exists properly
        if (roleRepository.findByRole(authority) == null || 
           (roleRepository.findByRole(authority) instanceof java.util.Optional && 
            ((java.util.Optional<?>) roleRepository.findByRole(authority)).isEmpty())) {

            System.out.println("Creating Role: " + authority); // DEBUG LOG
            Role role = new Role();
            role.setRole(authority);
            role.setIsStaff(isStaff);
            roleRepository.save(role);
        }
    }

    // 2. Fix seedStatus
    private void seedStatus(String statusName, boolean isInitial, boolean isFinal) {
        // Check if it exists. If your repo returns Optional, use .isEmpty() or .isPresent()
        // Here is a generic fix assuming it might be acting weird:
        var existing = shipmentStatusRepository.findByStatusName(statusName);

        boolean exists = false;
        if (existing != null) {
            if (existing instanceof java.util.Optional) {
                exists = ((java.util.Optional<?>) existing).isPresent();
            } else {
                exists = true; // It's a Role object and it's not null
            }
        }

        if (!exists) {
            System.out.println("Creating Status: " + statusName); // DEBUG LOG
            ShipmentStatus status = new ShipmentStatus();
            status.setStatusName(statusName);
            status.setIsInitial(isInitial);
            status.setIsFinal(isFinal);
            shipmentStatusRepository.save(status);
        }
    }

    // 3. Fix seedDeliveryType
    private void seedDeliveryType(String typeName, boolean requiresOffice) {
        var existing = deliveryTypeRepository.findByTypeName(typeName);

        boolean exists = false;
        if (existing != null) {
            if (existing instanceof java.util.Optional) {
                exists = ((java.util.Optional<?>) existing).isPresent();
            } else {
                exists = true;
            }
        }

        if (!exists) {
            System.out.println("Creating Type: " + typeName); // DEBUG LOG
            DeliveryType type = new DeliveryType();
            type.setTypeName(typeName);
            type.setRequiresOffice(requiresOffice);
            deliveryTypeRepository.save(type);
        }
    }
    
    private void seedAdminUser() {
        // 1. Check if admin already exists
        if (userRepository.findByUsername("admin").isPresent()) {
            return; // Skip if exists
        }

        System.out.println("Creating Admin User...");

        // 2. Create the User Object
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@mail.com");
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setEnabled(true);

        // 3. Hash the Password (CRITICAL)
        // We use "topsecret" (or whatever you want) as the raw password.
        // The encoder turns it into the "jGl25b..." hash automatically.
        admin.setPassword(passwordEncoder.encode("admin"));

        // 4. Assign the Role
        // We fetch the role object we just created earlier
        Role adminRole = roleRepository.findByRole("admin")
                .orElseThrow(() -> new IllegalStateException("CRITICAL ERROR: 'admin' role not found. Check seedRole() logic."));

        admin.setRole(adminRole);
        


        // 5. Save
        userRepository.save(admin);
    }
}

