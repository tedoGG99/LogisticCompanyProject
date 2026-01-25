package com.nbu.logistics.repository;

import com.company.logistics.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourierRepository extends JpaRepository<Courier, Integer> {

    List<Courier> findByOfficeId(Integer officeId);
}