package com.company.logistics.repository;

import com.company.logistics.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourierRepository extends JpaRepository<Courier, Integer> {

    List<Courier> findByOfficeId(Integer officeId);
}