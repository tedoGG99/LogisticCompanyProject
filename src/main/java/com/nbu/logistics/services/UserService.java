
package com.nbu.logistics.services;

import com.nbu.logistics.data.Office;
import com.nbu.logistics.data.Role;
import com.nbu.logistics.data.User;
import com.nbu.logistics.dto.UserRegistrationDto;
import com.nbu.logistics.repositories.OfficeRepository;
import com.nbu.logistics.repositories.RoleRepository;
import com.nbu.logistics.repositories.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    
    /**
     * Registers a new user.Expects role names: "client", "office_employee", "courier"
     * @param dto
     */
    public void registerUser(UserRegistrationDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(true);
        

        // 1. Assign Role (Exact match from DB)
        // We force lowercase to match your DB standard: "client", "office_employee"
        String roleName = dto.getRoleName().toLowerCase(); 
        
        Role role = roleRepository.findByRole(roleName).orElse(null
        );
        if (role == null) {
            // Default to client if something goes wrong
            role = new Role();
            role.setId(1);
            role.setRole("client");
        }
        user.setRole(role);
        
        // 2. Assign Office (Only for office_employee)
        if ("office employee".equals(roleName) && dto.getOfficeId() != 0)  {
            Office office = officeRepository.findById(dto.getOfficeId())
                    .orElseThrow(() -> new RuntimeException("Selected office not found"));
            user.setOffice(office);
        }

        userRepository.save(user);
    }
    
    public void saveUser(User user) {
        // Optional: Check for duplicates before saving
        if (user.getId() == null && userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Just save it. The Controller has already handled the Password and Role logic.
        userRepository.save(user);
    }
    
    public void updateUser(User formUser) {
        // Fetch the existing user from DB to preserve password
        User existingUser = userRepository.findById(formUser.getId()).orElse(null);

        if (existingUser != null) {
            // Update editable fields
            existingUser.setUsername(formUser.getUsername());
            existingUser.setEmail(formUser.getEmail());
            existingUser.setRole(formUser.getRole());
            existingUser.setOffice(formUser.getOffice());
            existingUser.setEnabled(formUser.isEnabled()); // Update status

            // Save back to DB
            userRepository.save(existingUser);
        }
    }
    
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Finds all clients for the "Sender" dropdown
     *
     * @return
     */
    public List<?> findAllClients() {
        Role clientRole = roleRepository.findByRole("client").orElse(null);
        if(clientRole == null){
            return null;
        }
        return userRepository.findAllByRole(clientRole);
    }

    /**
     * Finds all employees (Office employees + Couriers) for the Reference/Report
     *
     * @return
     */
    public List<?> findAllEmployees() {
        Role officeRole = roleRepository.findByRole("office employee").orElse(null);
        Role courierRole = roleRepository.findByRole("courier").orElse(null);
        
        if(officeRole == null && courierRole == null){
            return null;
        }
        
        List<User> employees = userRepository.findAllByRole(officeRole);
        employees.addAll(userRepository.findAllByRole(courierRole));
        
        return employees;
    }

    public void deleteUser(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(false);
        userRepository.save(user);
    }
    
    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }
    
    public Role getAdminRole(){
        return roleRepository.findByRole("admin").orElse(null);
    }
    
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    
    public User getUserById(Integer id){
        return userRepository.findById(id).orElse(null);
    }
    
    public boolean userExists(String username) {
        // Assuming your repository returns Optional<User>
        return userRepository.findByUsername(username).isPresent();
    }
    
    
    
    
}
