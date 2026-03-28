package com.hrms.employee.service;

import com.hrms.employee.dto.*;
import com.hrms.employee.entity.Designation;
import com.hrms.employee.repository.DesignationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DesignationService {

    private final DesignationRepository repo;

    public DesignationResponseDTO create(DesignationCreateDTO dto) {

        if (repo.existsByName(dto.getName())) {
            throw new RuntimeException("Designation already exists");
        }

        Designation d = Designation.builder()
                .name(dto.getName())
                .level(dto.getLevel())
                .build();

        return map(repo.save(d));
    }

    public List<DesignationResponseDTO> getAll() {
        return repo.findAllByOrderByLevelAsc()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private DesignationResponseDTO map(Designation d) {
        return DesignationResponseDTO.builder()
                .id(d.getId())
                .name(d.getName())
                .level(d.getLevel())
                .build();
    }
}
