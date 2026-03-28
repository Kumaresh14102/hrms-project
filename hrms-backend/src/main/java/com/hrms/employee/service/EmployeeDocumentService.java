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
public class EmployeeDocumentService {

    private final EmployeeRepository employeeRepo;
    private final EmployeeDocumentRepository documentRepo;

    public EmployeeDocumentResponseDTO addDocument(EmployeeDocumentCreateDTO dto) {

        Employee emp = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        EmployeeDocument doc = EmployeeDocument.builder()
                .employee(emp)
                .documentType(dto.getDocumentType())
                .filePath(dto.getFilePath())
                .build();

        return map(documentRepo.save(doc));
    }

    public List<EmployeeDocumentResponseDTO> getDocuments(Long employeeId) {
        return documentRepo.findByEmployeeId(employeeId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public void deleteDocument(Long id) {
        documentRepo.deleteById(id);
    }

    private EmployeeDocumentResponseDTO map(EmployeeDocument d) {
        return EmployeeDocumentResponseDTO.builder()
                .id(d.getId())
                .documentType(d.getDocumentType())
                .filePath(d.getFilePath())
                .uploadedAt(d.getUploadedAt())
                .build();
    }
}
