package com.company.logistics.repository;

import com.company.logistics.entity.OfficeEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeEmployeeRepository extends JpaRepository<OfficeEmployee, Integer> {
}//