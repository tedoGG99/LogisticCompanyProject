package com.nbu.logistics.serviceTest;

import com.nbu.logistics.data.SystemParameter;
import com.nbu.logistics.repositories.SystemParameterRepository;
import com.nbu.logistics.services.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PricingServiceTest {

    @Mock
    private SystemParameterRepository repo;

    @InjectMocks
    private PricingService pricingService;

    private SystemParameter param;

    @BeforeEach
    void setUp() {
        param = new SystemParameter();
    }

    // ------------------------
    // BASE PRICE
    // ------------------------
    @Test
    void getBasePrice_whenPresent_shouldReturnValueFromRepo() {
        param.setValue("7.50");
        when(repo.findById("BASE_PRICE")).thenReturn(Optional.of(param));

        BigDecimal result = pricingService.getBasePrice();

        assertEquals(new BigDecimal("7.50"), result);
    }

    @Test
    void getBasePrice_whenMissing_shouldReturnDefault() {
        when(repo.findById("BASE_PRICE")).thenReturn(Optional.empty());

        BigDecimal result = pricingService.getBasePrice();

        assertEquals(new BigDecimal("5.00"), result);
    }

    // ------------------------
    // WEIGHT FACTOR
    // ------------------------
    @Test
    void getWeightFactor_whenPresent_shouldReturnValueFromRepo() {
        param.setValue("3.25");
        when(repo.findById("WEIGHT_FACTOR")).thenReturn(Optional.of(param));

        BigDecimal result = pricingService.getWeightFactor();

        assertEquals(new BigDecimal("3.25"), result);
    }

    @Test
    void getWeightFactor_whenMissing_shouldReturnDefault() {
        when(repo.findById("WEIGHT_FACTOR")).thenReturn(Optional.empty());

        BigDecimal result = pricingService.getWeightFactor();

        assertEquals(new BigDecimal("2.00"), result);
    }

    // ------------------------
    // ADDRESS SURCHARGE
    // ------------------------
    @Test
    void getAddressSurcharge_whenPresent_shouldReturnValueFromRepo() {
        param.setValue("15.00");
        when(repo.findById("ADDRESS_SURCHARGE")).thenReturn(Optional.of(param));

        BigDecimal result = pricingService.getAddressSurcharge();

        assertEquals(new BigDecimal("15.00"), result);
    }

    @Test
    void getAddressSurcharge_whenMissing_shouldReturnDefault() {
        when(repo.findById("ADDRESS_SURCHARGE")).thenReturn(Optional.empty());

        BigDecimal result = pricingService.getAddressSurcharge();

        assertEquals(new BigDecimal("10.00"), result);
    }
}
