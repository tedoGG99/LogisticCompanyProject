package com.company.logistics.repository;

import com.company.logistics.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeRepository extends JpaRepository<Office, Integer> {

    List<Office> findByCompanyId(Integer companyId);
}