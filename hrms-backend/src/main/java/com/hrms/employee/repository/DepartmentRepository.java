package com.hrms.employee.repository;

import com.hrms.employee.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByParentId(Long parentId);

    boolean existsByName(String name);
}
