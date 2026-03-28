package com.hrms.employee.repository;

import com.hrms.employee.entity.EmployeeAddress;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeAddressRepository extends JpaRepository<EmployeeAddress, Long>{
	
	List<EmployeeAddress> findByEmployeeId(Long employeeId);
	
	List<EmployeeAddress> findByEmployeeIdAndType(Long employeeId, String type);
	
	void deleteByEmployeeIdAndType(Long employeeId, String type);

}
