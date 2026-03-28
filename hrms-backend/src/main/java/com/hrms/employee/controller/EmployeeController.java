package com.hrms.employee.controller;

import com.hrms.employee.dto.EmployeeCreateDTO;
import com.hrms.employee.dto.EmployeeResponseDTO;
import com.hrms.employee.entity.Employee;
import com.hrms.employee.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@CrossOrigin
public class EmployeeController {
	
	private final EmployeeService employeeService;
	
	@PostMapping
    public EmployeeResponseDTO createEmployee(
            @Valid @RequestBody EmployeeCreateDTO request) {

        Employee emp = employeeService.createEmployee(request);

        return mapToResponse(emp);
    }
	
	@GetMapping
    public List<EmployeeResponseDTO> getEmployees() {

        return employeeService.getActiveEmployees()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
	
	@GetMapping("/all")
	public List<EmployeeResponseDTO> getAllEmployees() {

	    return employeeService.getAllEmployees()
	            .stream()
	            .map(this::mapToResponse)
	            .collect(Collectors.toList());
	}
	
	@GetMapping("/{employeeId}")
    public EmployeeResponseDTO getByEmployeeId(@PathVariable String employeeId) {

        Employee emp = employeeService.getByEmployeeId(employeeId);

        return mapToResponse(emp);
    }
	
	@PutMapping("/{employeeId}/exit")
    public String exitEmployee(@PathVariable String employeeId) {

        employeeService.exitEmployee(employeeId);

        return "Employee exited successfully";
    }
	
	private EmployeeResponseDTO mapToResponse(Employee emp) {

	    return EmployeeResponseDTO.builder()
	            .id(emp.getId())
	            .employeeCode(emp.getEmployeeCode())

	            .firstName(emp.getFirstName())
	            .lastName(emp.getLastName())

	            .email(emp.getEmail())
	            .mobile(emp.getMobile())

	            .gender(emp.getGender())
	            .dob(emp.getDob())

	            .departmentId(emp.getDepartmentId())
	            .designationId(emp.getDesignationId())
	            .managerId(emp.getManagerId())

	            .dateOfJoining(emp.getDateOfJoining())
	            .probationEndDate(emp.getProbationEndDate())

	            .employmentType(emp.getEmploymentType())
	            .status(emp.getStatus())

	            .maritalStatus(emp.getMaritalStatus())
	            .bloodGroup(emp.getBloodGroup())
	            .profilePhotoUrl(emp.getProfilePhotoUrl())

	            .createdAt(emp.getCreatedAt())
	            .build();
	}



}
