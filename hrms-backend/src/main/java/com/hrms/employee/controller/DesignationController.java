package com.hrms.employee.controller;

import com.hrms.employee.dto.*;
import com.hrms.employee.service.DesignationService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/designations")
@RequiredArgsConstructor
@CrossOrigin
public class DesignationController {

    private final DesignationService service;

    @PostMapping
    public DesignationResponseDTO create(@RequestBody DesignationCreateDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<DesignationResponseDTO> all() {
        return service.getAll();
    }
}
