package com.hrms.employee.controller;

import com.hrms.employee.dto.*;
import com.hrms.employee.service.EmployeeDocumentService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@CrossOrigin
public class EmployeeDocumentController {

    private final EmployeeDocumentService service;

    @PostMapping
    public EmployeeDocumentResponseDTO add(@RequestBody EmployeeDocumentCreateDTO dto) {
        return service.addDocument(dto);
    }

    @GetMapping("/{employeeId}")
    public List<EmployeeDocumentResponseDTO> get(@PathVariable Long employeeId) {
        return service.getDocuments(employeeId);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteDocument(id);
        return "Deleted successfully";
    }
}
