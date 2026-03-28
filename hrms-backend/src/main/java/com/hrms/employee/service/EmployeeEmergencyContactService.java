package com.hrms.employee.service;

import com.hrms.employee.dto.*;
import com.hrms.employee.entity.*;
import com.hrms.employee.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeEmergencyContactService {

    private final EmployeeRepository employeeRepo;
    private final EmployeeEmergencyContactRepository contactRepo;

    public EmployeeEmergencyContactResponseDTO add(EmployeeEmergencyContactCreateDTO dto) {

        Employee emp = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        EmployeeEmergencyContact contact = EmployeeEmergencyContact.builder()
                .employee(emp)
                .name(dto.getName())
                .relation(dto.getRelation())
                .phone(dto.getPhone())
                .build();

        return map(contactRepo.save(contact));
    }

    public List<EmployeeEmergencyContactResponseDTO> get(Long employeeId) {
        return contactRepo.findByEmployeeId(employeeId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        contactRepo.deleteById(id);
    }

    private EmployeeEmergencyContactResponseDTO map(EmployeeEmergencyContact c) {
        return EmployeeEmergencyContactResponseDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .relation(c.getRelation())
                .phone(c.getPhone())
                .build();
    }
}
