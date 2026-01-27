
package com.nbu.logistics.services;

import com.nbu.logistics.repositories.SystemParameterRepository;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PricingService {

    @Autowired
    private SystemParameterRepository repo;

    // Fallbacks in case DB is empty
    private static final BigDecimal DEFAULT_BASE = new BigDecimal("5.00");
    private static final BigDecimal DEFAULT_WEIGHT = new BigDecimal("2.00");
    private static final BigDecimal DEFAULT_SURCHARGE = new BigDecimal("10.00");

    public BigDecimal getBasePrice() {
        return getDecimalParam("BASE_PRICE", DEFAULT_BASE);
    }

    public BigDecimal getWeightFactor() {
        return getDecimalParam("WEIGHT_FACTOR", DEFAULT_WEIGHT);
    }

    public BigDecimal getAddressSurcharge() {
        return getDecimalParam("ADDRESS_SURCHARGE", DEFAULT_SURCHARGE);
    }

    private BigDecimal getDecimalParam(String key, BigDecimal defaultValue) {
        return repo.findById(key)
                .map(param -> new BigDecimal(param.getValue()))
                .orElse(defaultValue);
    }
}
