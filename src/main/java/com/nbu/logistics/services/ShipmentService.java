
package com.nbu.logistics.services;

import com.nbu.logistics.data.DeliveryType;
import com.nbu.logistics.data.Office;
import com.nbu.logistics.data.Shipment;
import com.nbu.logistics.data.ShipmentStatus;
import com.nbu.logistics.data.User;
import com.nbu.logistics.dto.ShipmentDto;
import com.nbu.logistics.repositories.DeliveryTypeRepository;
import com.nbu.logistics.repositories.OfficeRepository;
import com.nbu.logistics.repositories.ShipmentRepository;
import com.nbu.logistics.repositories.ShipmentStatusRepository;
import com.nbu.logistics.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;



@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private DeliveryTypeRepository deliveryTypeRepository;
    @Autowired
    private ShipmentStatusRepository shipmentStatusRepository;
    
    @Autowired 
    private PricingService pricingService;

//    // PRICING CONSTANTS
//    private static final BigDecimal BASE_PRICE = new BigDecimal("5.00");
//    private static final BigDecimal WEIGHT_FACTOR = new BigDecimal("2.00");
//    private static final BigDecimal ADDRESS_SURCHARGE = new BigDecimal("10.00");

    /**
     * Creates a new Shipment based on the DTO.
     */
    public void createShipment(ShipmentDto dto, User creator) {
        Shipment shipment = new Shipment();

        // ---------------------------------------------------------
        // 1. BASIC SETUP
        // ---------------------------------------------------------
        shipment.setTrackingNumber(UUID.randomUUID().toString());
        shipment.setDateRegistered(LocalDateTime.now());
        shipment.setWeight(dto.getWeight());

        // ---------------------------------------------------------
        // 2. SENDER & AUDIT LOGIC (Dynamic Role Check)
        // ---------------------------------------------------------
        // We check the DB flag 'is_staff' instead of hardcoded string "ROLE_EMPLOYEE"
        boolean isStaff = Boolean.TRUE.equals(creator.getRole().getIsStaff());

        if (isStaff) {
            // SCENARIO A: Employee is at the desk registering a package for a customer.
            // usage: Employee selects the "Sender" from a dropdown list.
            User sender = userRepository.findById(dto.getSenderId())
                    .orElseThrow(() -> new IllegalArgumentException("Sender ID not found"));
            shipment.setSender(sender);
            
            // Audit: Record that THIS employee performed the action
            shipment.setEmployee(creator); 
        } else {
            // SCENARIO B: Client is logged in at home.
            // Security: We IGNORE the senderId from DTO (to prevent hacking) 
            // and force the sender to be the logged-in user.
            shipment.setSender(creator);
        }

        // ---------------------------------------------------------
        // 3. RECEIVER LOGIC (UPDATED)
        // ---------------------------------------------------------
        
        // CASE A: Sending to a Registered User
        if (dto.getReceiverUsername() != null && !dto.getReceiverUsername().isEmpty()) {
            
            User receiver = userRepository.findByUsername(dto.getReceiverUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Receiver username not found"));
            
            shipment.setReceiver(receiver);


            if (dto.getReceiverName() == null || dto.getReceiverName().trim().isEmpty()) {
                shipment.setReceiverName(receiver.getUsername());
            } else {
                // If they typed a specific name (e.g. "To Mom"), keep it
                shipment.setReceiverName(dto.getReceiverName());
            }

        } else {
            // CASE B: Sending to a Guest (No account)
            // We must use whatever name was typed in the text box
            shipment.setReceiverName(dto.getReceiverName());
        }

        // Phone is always required/copied
        shipment.setReceiverPhone(dto.getReceiverPhone());

        // ---------------------------------------------------------
        // 4. DELIVERY TYPE LOGIC (Dynamic Flag Check)
        // ---------------------------------------------------------
        DeliveryType type = deliveryTypeRepository.findById(dto.getDeliveryTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Delivery Type ID"));
        
        shipment.setDeliveryTypeId(type);

        // Check DB flag: Does this type require an Office? (e.g. TO_OFFICE = true)
        if (Boolean.TRUE.equals(type.getRequiresOffice())) {
            if (dto.getTargetOfficeId() == null) {
                throw new IllegalArgumentException("Office ID is required for this delivery type");
            }
            Office office = officeRepository.findById(dto.getTargetOfficeId())
                    .orElseThrow(() -> new IllegalArgumentException("Office not found"));
            shipment.setOffice(office);
            shipment.setDeliveryAddress(office.getFullLocation());
        } else {
            // If it doesn't require office, we assume it requires an Address (TO_HOME)
            if (dto.getTargetAddress() == null || dto.getTargetAddress().isEmpty()) {
                throw new IllegalArgumentException("Target Address is required for this delivery type");
            }
            shipment.setDeliveryAddress(dto.getTargetAddress());
        }

        // ---------------------------------------------------------
        // 5. STATUS LOGIC (Dynamic Initial Status)
        // ---------------------------------------------------------
        // We find the status where 'is_initial' is TRUE in the database
        ShipmentStatus initialStatus = shipmentStatusRepository.findFirstByIsInitialTrue()
                .orElseThrow(() -> new IllegalStateException("System Config Error: No status marked as 'is_initial' in DB"));
        
        shipment.setStatus(initialStatus);

        // ---------------------------------------------------------
        // 6. PRICING LOGIC (Dynamic DB Configuration)
        // ---------------------------------------------------------
        BigDecimal basePrice = pricingService.getBasePrice();      // e.g. 5.00
        BigDecimal weightFactor = pricingService.getWeightFactor(); // e.g. 2.00
        BigDecimal surcharge = pricingService.getAddressSurcharge(); // e.g. 10.00

        // Formula: (Weight * Factor) + Base
        BigDecimal price = BigDecimal.valueOf(dto.getWeight()).multiply(weightFactor).add(basePrice);
        
        // Add surcharge if NOT going to an office
        if (!Boolean.TRUE.equals(type.getRequiresOffice())) {
            price = price.add(surcharge);
        }
        shipment.setPrice(price);

        // ---------------------------------------------------------
        // 7. FINAL SAVE
        // ---------------------------------------------------------
        shipmentRepository.save(shipment);
    }

    // VISIBILITY LOGIC
    public List<Shipment> findShipmentsForUser(User user) {
        // Assuming User entity has a getRole() method that returns a Role entity or String
        String roleName = user.getRole().getRole(); 

        if (null == roleName) {
            // Client sees what they sent AND what is coming to them
            List<Shipment> sent = shipmentRepository.findBySender(user);
            List<Shipment> received = shipmentRepository.findByReceiver(user);
            sent.addAll(received);
            return sent;
        } else switch (roleName) {
            case "ROLE_OFFICE_EMPLOYEE", "ROLE_ADMIN" -> {
                return shipmentRepository.findAll();
            }
            case "ROLE_COURIER" -> {
                return shipmentRepository.findByCourier(user);
            }
            default -> {
                // Client sees what they sent AND what is coming to them
                List<Shipment> sent = shipmentRepository.findBySender(user);
                List<Shipment> received = shipmentRepository.findByReceiver(user);
                sent.addAll(received);
                return sent;
            }
        }
    }

    public void updateStatus(int shipmentId, String newStatusName, String username) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));

        // 1. Convert String "SENT" -> Database Entity
        // (Make sure your Repository has findByStatusName)
        ShipmentStatus status = shipmentStatusRepository.findByStatusName(newStatusName)
                .orElseThrow(() -> new IllegalArgumentException("Status not found: " + newStatusName));

        shipment.setStatus(status);

        // 2. Assign Employee (if not already assigned)
        if (shipment.getEmployee() == null && username != null) {
            User currentUser = userRepository.findByUsername(username).orElse(null);
            shipment.setEmployee(currentUser);
        }

        // 3. Mark Date if Delivered
        if ("DELIVERED".equalsIgnoreCase(newStatusName)) {
            shipment.setDateDelivered(LocalDateTime.now());
        }

        shipmentRepository.save(shipment);
    }
    // DELETE
    public void deleteShipment(int id) {
        shipmentRepository.deleteById(id);
    }

    // REPORT: REVENUE
    public BigDecimal calculateRevenue(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        BigDecimal total = shipmentRepository.calculateRevenue(startDateTime, endDateTime);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    
    public List<Shipment> findShipmentsByEmployee(int employeeId) {
        User employee = userRepository.findById(employeeId)
            .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));

        // Validation: Is this user actually an employee?
        // (Assuming you have role logic, otherwise skip this check)
        // if (!"ROLE_OFFICE_EMPLOYEE".equals(employee.getRole().getRoleName())) {
        //    throw new IllegalArgumentException("User is not an office employee");
        // }

        if (employee.getOffice() == null) {
            throw new IllegalStateException("Employee is not assigned to any office");
        }

        return shipmentRepository.findByOffice(employee.getOffice());
    }

    /**
     * Report: Find all shipments that are NOT delivered.
     */
    public List<Shipment> findUnreceivedShipments() {
        // You can either use a custom @Query in Repository OR filter in Java.
        // Custom Query is much faster.
        return shipmentRepository.findAllUnreceivedShipments();
    }
    
    public List<Shipment> findShipmentsByClient(int clientId) {
        User client = userRepository.findById(clientId)
            .orElseThrow(() -> new IllegalArgumentException("Client not found with ID: " + clientId));

        // 1. Get shipments sent by this client
        List<Shipment> sent = shipmentRepository.findBySender(client);

        // 2. Get shipments received by this client (if they are a registered user)
        List<Shipment> received = shipmentRepository.findByReceiver(client);

        // 3. Combine them
        sent.addAll(received);
        
        // Optional: Sort by date (newest first)
        // sent.sort((s1, s2) -> s2.getDateRegistered().compareTo(s1.getDateRegistered()));

        return sent;
    }
    
    public Shipment getShipmentById(Integer id){
        return shipmentRepository.findById(id).orElse(null);
    }
}
