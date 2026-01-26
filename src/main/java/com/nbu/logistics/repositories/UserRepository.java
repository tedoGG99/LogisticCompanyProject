
package com.nbu.logistics.repositories;

import com.nbu.logistics.data.Role;
import com.nbu.logistics.data.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByUsername(String username);
    List<User> findAllByRole(Role role);

}
