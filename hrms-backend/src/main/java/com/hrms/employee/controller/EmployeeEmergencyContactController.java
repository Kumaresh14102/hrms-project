package com.hrms.employee.controller;

import com.hrms.employee.dto.*;
import com.hrms.employee.service.EmployeeEmergencyContactService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency-contacts")
@RequiredArgsConstructor
@CrossOrigin
public class EmployeeEmergencyContactController {

    private final EmployeeEmergencyContactService service;

    @PostMapping
    public EmployeeEmergencyContactResponseDTO add(@RequestBody EmployeeEmergencyContactCreateDTO dto) {
        return service.add(dto);
    }

    @GetMapping("/{employeeId}")
    public List<EmployeeEmergencyContactResponseDTO> get(@PathVariable Long employeeId) {
        return service.get(employeeId);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Deleted successfully";
    }
}
