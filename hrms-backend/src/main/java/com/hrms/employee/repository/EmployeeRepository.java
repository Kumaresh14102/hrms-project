package com.hrms.employee.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.employee.entity.Employee;

import java.util.List;
import java.util.Optional;
public interface EmployeeRepository extends JpaRepository<Employee,Long>{
	
	Optional<Employee>findByEmail(String email);
	
	boolean existsByEmail(String email);
	
	Optional<Employee>findByEmployeeCode(String employeeCode);
	
	List<Employee>findByStatus(String status);

}
