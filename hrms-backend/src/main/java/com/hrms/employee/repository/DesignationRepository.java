package com.hrms.employee.repository;

import com.hrms.employee.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesignationRepository extends JpaRepository<Designation, Long> {

    boolean existsByName(String name);

    List<Designation> findAllByOrderByLevelAsc();
}
