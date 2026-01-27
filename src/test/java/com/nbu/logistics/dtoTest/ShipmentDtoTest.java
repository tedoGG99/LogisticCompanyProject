package com.nbu.logistics.dtoTest;

import com.nbu.logistics.dto.ShipmentDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentDtoTest {

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
    void validShipmentDto_shouldHaveNoViolations() {
        ShipmentDto dto = new ShipmentDto(
                1,
                1,
                "receiverUser",
                "Receiver Name",
                "0888123456",
                2.5,
                1,
                null,
                null,
                "Sofia, Studentski grad"
        );

        Set<ConstraintViolation<ShipmentDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    // ------------------------
    // senderId = null
    // ------------------------
    @Test
    void nullSenderId_shouldFailValidation() {
        ShipmentDto dto = new ShipmentDto();
        dto.setWeight(2.0);
        dto.setDeliveryTypeId(1);

        Set<ConstraintViolation<ShipmentDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("senderId"))
        );
    }

    // ------------------------
    // weight <= 0
    // ------------------------
    @Test
    void negativeWeight_shouldFailValidation() {
        ShipmentDto dto = new ShipmentDto();
        dto.setSenderId(1);
        dto.setDeliveryTypeId(1);
        dto.setWeight(-1.0);

        Set<ConstraintViolation<ShipmentDto>> violations = validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("weight"))
        );
    }

    // ------------------------
    // deliveryTypeId = null
    // ------------------------
    @Test
    void nullDeliveryTypeId_shouldFailValidation() {
        ShipmentDto dto = new ShipmentDto();
        dto.setSenderId(1);
        dto.setWeight(2.0);

        Set<ConstraintViolation<ShipmentDto>> violations = validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("deliveryTypeId"))
        );
    }

    // ------------------------
    // receiverName too short
    // ------------------------
    @Test
    void emptyReceiverName_shouldFailValidation() {
        ShipmentDto dto = new ShipmentDto();
        dto.setSenderId(1);
        dto.setWeight(2.0);
        dto.setDeliveryTypeId(1);
        dto.setReceiverName("");

        Set<ConstraintViolation<ShipmentDto>> violations = validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("receiverName"))
        );
    }

    // ------------------------
    // targetAddress too short
    // ------------------------
    @Test
    void shortTargetAddress_shouldFailValidation() {
        ShipmentDto dto = new ShipmentDto();
        dto.setSenderId(1);
        dto.setWeight(2.0);
        dto.setDeliveryTypeId(1);
        dto.setTargetAddress("abc");

        Set<ConstraintViolation<ShipmentDto>> violations = validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("targetAddress"))
        );
    }
}
