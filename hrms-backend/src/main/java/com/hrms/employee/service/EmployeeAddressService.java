package com.hrms.employee.service;

import com.hrms.employee.dto.EmployeeAddressCreateDTO;
import com.hrms.employee.dto.EmployeeAddressResponseDTO;
import com.hrms.employee.entity.Employee;
import com.hrms.employee.entity.EmployeeAddress;
import com.hrms.employee.repository.EmployeeAddressRepository;
import com.hrms.employee.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeAddressService {
	
	private final EmployeeAddressRepository addressRepo;
    private final EmployeeRepository employeeRepo;
    
    public EmployeeAddressResponseDTO addAddress(EmployeeAddressCreateDTO dto) {

        Employee emp = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // business rule: only 1 permanent allowed
        if ("PERMANENT".equalsIgnoreCase(dto.getType())) {
            addressRepo.deleteByEmployeeIdAndType(dto.getEmployeeId(), "PERMANENT");
        }

        EmployeeAddress address = EmployeeAddress.builder()
                .employee(emp)
                .type(dto.getType())
                .line1(dto.getLine1())
                .line2(dto.getLine2())
                .city(dto.getCity())
                .state(dto.getState())
                .pincode(dto.getPincode())
                .country(dto.getCountry())
                .build();

        EmployeeAddress saved = addressRepo.save(address);

        return mapToResponse(saved);
    }
    
    public List<EmployeeAddressResponseDTO> getEmployeeAddresses(Long employeeId) {

        return addressRepo.findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public void deleteAddress(Long addressId) {
        addressRepo.deleteById(addressId);
    }
    
    private EmployeeAddressResponseDTO mapToResponse(EmployeeAddress a) {

        return EmployeeAddressResponseDTO.builder()
                .id(a.getId())
                .type(a.getType())
                .line1(a.getLine1())
                .line2(a.getLine2())
                .city(a.getCity())
                .state(a.getState())
                .pincode(a.getPincode())
                .country(a.getCountry())
                .build();
    }

}
