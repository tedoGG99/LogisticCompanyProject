package com.nbu.logistics.dtoTest;

import com.nbu.logistics.dto.UserRegistrationDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ------------------------
    // VALID DTO
    // ------------------------
    @Test
    void validUserRegistrationDto_shouldHaveNoViolations() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("valid_user-123");
        dto.setEmail("test@example.com");
        dto.setPassword("secret");
        dto.setFirstName("Ivan");
        dto.setLastName("Petrov");
        dto.setRoleName("client");
        dto.setOfficeId(0);

        Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    // ------------------------
    // INVALID USERNAME
    // ------------------------
    @Test
    void invalidUsernameCharacters_shouldFailValidation() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("invalid@user");
        dto.setEmail("test@example.com");
        dto.setPassword("pass");
        dto.setFirstName("Ivan");
        dto.setLastName("Petrov");
        dto.setRoleName("client");

        Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("username"))
        );
    }

    // ------------------------
    // INVALID EMAIL
    // ------------------------
    @Test
    void invalidEmail_shouldFailValidation() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("validuser");
        dto.setEmail("not-an-email");
        dto.setPassword("pass");
        dto.setFirstName("Ivan");
        dto.setLastName("Petrov");
        dto.setRoleName("client");

        Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("email"))
        );
    }

    // ------------------------
    // BLANK PASSWORD
    // ------------------------
    @Test
    void blankPassword_shouldFailValidation() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("validuser");
        dto.setEmail("test@example.com");
        dto.setPassword("");
        dto.setFirstName("Ivan");
        dto.setLastName("Petrov");
        dto.setRoleName("client");

        Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("password"))
        );
    }

    // ------------------------
    // INVALID ROLE
    // ------------------------
    @Test
    void invalidRole_shouldFailValidation() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("validuser");
        dto.setEmail("test@example.com");
        dto.setPassword("pass");
        dto.setFirstName("Ivan");
        dto.setLastName("Petrov");
        dto.setRoleName("manager"); // ❌ not allowed

        Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("roleName"))
        );
    }

    // ------------------------
    // NEGATIVE OFFICE ID
    // ------------------------
    @Test
    void negativeOfficeId_shouldFailValidation() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("validuser");
        dto.setEmail("test@example.com");
        dto.setPassword("pass");
        dto.setFirstName("Ivan");
        dto.setLastName("Petrov");
        dto.setRoleName("office employee");
        dto.setOfficeId(-1);

        Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("officeId"))
        );
    }
}
