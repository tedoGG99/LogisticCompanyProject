package com.nbu.logistics.serviceTest;

import com.nbu.logistics.data.Office;
import com.nbu.logistics.data.Role;
import com.nbu.logistics.data.User;
import com.nbu.logistics.dto.UserRegistrationDto;
import com.nbu.logistics.repositories.OfficeRepository;
import com.nbu.logistics.repositories.RoleRepository;
import com.nbu.logistics.repositories.UserRepository;
import com.nbu.logistics.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private OfficeRepository officeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRegistrationDto dto;
    private Role clientRole;
    private Role officeRole;
    private Office office;

    @BeforeEach
    void setUp() {
        dto = new UserRegistrationDto();
        dto.setUsername("testuser");
        dto.setEmail("test@test.com");
        dto.setPassword("secret");
        dto.setFirstName("Ivan");
        dto.setLastName("Petrov");
        dto.setRoleName("client");
        dto.setOfficeId(0);

        clientRole = new Role();
        clientRole.setId(1);
        clientRole.setRole("client");

        officeRole = new Role();
        officeRole.setId(2);
        officeRole.setRole("office employee");

        office = new Office();
        office.setId(1);
    }

    // -------------------------------------------------
    // registerUser – SUCCESS
    // -------------------------------------------------
    @Test
    void registerUser_validClient_shouldSaveUser() {
        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret"))
                .thenReturn("ENCODED");
        when(roleRepository.findByRole("client"))
                .thenReturn(Optional.of(clientRole));

        userService.registerUser(dto);

        verify(userRepository).save(any(User.class));
    }

    // -------------------------------------------------
    // registerUser – DUPLICATE USERNAME
    // -------------------------------------------------
    @Test
    void registerUser_existingUsername_shouldThrow() {
        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class,
                () -> userService.registerUser(dto));

        verify(userRepository, never()).save(any());
    }

    // -------------------------------------------------
    // registerUser – OFFICE EMPLOYEE WITH OFFICE
    // -------------------------------------------------
    @Test
    void registerUser_officeEmployee_shouldAssignOffice() {
        dto.setRoleName("office employee");
        dto.setOfficeId(1);

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(any()))
                .thenReturn("ENCODED");
        when(roleRepository.findByRole("office employee"))
                .thenReturn(Optional.of(officeRole));
        when(officeRepository.findById(1))
                .thenReturn(Optional.of(office));

        userService.registerUser(dto);

        verify(userRepository).save(argThat(user ->
                user.getOffice() != null &&
                        user.getOffice().getId() == 1
        ));
    }

    // -------------------------------------------------
    // findByUsername – FOUND
    // -------------------------------------------------
    @Test
    void findByUsername_existingUser_shouldReturnUser() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        User result = userService.findByUsername("testuser");

        assertEquals("testuser", result.getUsername());
    }

    // -------------------------------------------------
    // findByUsername – NOT FOUND
    // -------------------------------------------------
    @Test
    void findByUsername_missingUser_shouldThrow() {
        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.findByUsername("missing"));
    }

    // -------------------------------------------------
    // findAllClients
    // -------------------------------------------------
    @Test
    void findAllClients_shouldReturnList() {
        when(roleRepository.findByRole("client"))
                .thenReturn(Optional.of(clientRole));
        when(userRepository.findAllByRole(clientRole))
                .thenReturn(List.of(new User()));

        List<?> result = userService.findAllClients();

        assertEquals(1, result.size());
    }

    // -------------------------------------------------
    // deleteUser – SOFT DELETE
    // -------------------------------------------------
    @Test
    void deleteUser_shouldDisableUser() {
        User user = new User();
        user.setEnabled(true);

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        userService.deleteUser(1);

        assertFalse(user.isEnabled());
        verify(userRepository).save(user);
    }

    // -------------------------------------------------
    // userExists
    // -------------------------------------------------
    @Test
    void userExists_shouldReturnTrue() {
        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(new User()));

        assertTrue(userService.userExists("testuser"));
    }
}
