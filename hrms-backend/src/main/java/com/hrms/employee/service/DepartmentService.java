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
public class DepartmentService {

    private final DepartmentRepository repo;

    public DepartmentResponseDTO create(DepartmentCreateDTO dto) {

        if (repo.existsByName(dto.getName())) {
            throw new RuntimeException("Department already exists");
        }

        Department parent = null;

        if (dto.getParentId() != null) {
            parent = repo.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));
        }

        Department d = Department.builder()
                .name(dto.getName())
                .parent(parent)
                .build();

        return map(repo.save(d));
    }

    public List<DepartmentResponseDTO> getAll() {
        return repo.findAll().stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<DepartmentResponseDTO> getChildren(Long parentId) {
        return repo.findByParentId(parentId).stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private DepartmentResponseDTO map(Department d) {
        return DepartmentResponseDTO.builder()
                .id(d.getId())
                .name(d.getName())
                .parentId(d.getParent() != null ? d.getParent().getId() : null)
                .build();
    }
}
