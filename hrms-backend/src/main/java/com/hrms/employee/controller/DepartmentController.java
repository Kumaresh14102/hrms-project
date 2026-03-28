package com.hrms.employee.controller;

import com.hrms.employee.dto.*;
import com.hrms.employee.service.DepartmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@CrossOrigin
public class DepartmentController {

    private final DepartmentService service;

    @PostMapping
    public DepartmentResponseDTO create(@RequestBody DepartmentCreateDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<DepartmentResponseDTO> all() {
        return service.getAll();
    }

    @GetMapping("/children/{parentId}")
    public List<DepartmentResponseDTO> children(@PathVariable Long parentId) {
        return service.getChildren(parentId);
    }
}
