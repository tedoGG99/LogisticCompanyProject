
package com.nbu.logistics.repositories;

import com.nbu.logistics.data.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Integer>{
    Optional<Role> findByRole(String role);
    
}
