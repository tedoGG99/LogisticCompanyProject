package com.company.logistics.repository;

import com.company.logistics.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    List<Address> findByCity(String city);

    List<Address> findByPostalCode(String postalCode);
}
