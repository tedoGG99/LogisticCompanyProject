package com.company.logistics.repository;

import com.company.logistics.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TariffRepository extends JpaRepository<Tariff, Integer> {

    Optional<Tariff> findFirstByDeliveryTypeIdAndValidFromLessThanEqualAndValidToGreaterThanEqual(
            Integer deliveryTypeId,
            LocalDate from,
            LocalDate to
    );
}//