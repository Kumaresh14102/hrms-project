package com.hrms.employee.controller;

import com.hrms.employee.dto.EmployeeAddressCreateDTO;
import com.hrms.employee.dto.EmployeeAddressResponseDTO;
import com.hrms.employee.service.EmployeeAddressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api")
public class EmployeeAddressController {

    private final EmployeeAddressService addressService;

    @PostMapping("/employees/{employeeId}/addresses")
    public EmployeeAddressResponseDTO addAddress(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeAddressCreateDTO dto) {

        dto.setEmployeeId(employeeId);

        return addressService.addAddress(dto);
    }

    @GetMapping("/employees/{employeeId}/addresses")
    public List<EmployeeAddressResponseDTO> getAddresses(
            @PathVariable Long employeeId) {

        return addressService.getEmployeeAddresses(employeeId);
    }

    @DeleteMapping("/addresses/{addressId}")
    public String deleteAddress(@PathVariable Long addressId) {

        addressService.deleteAddress(addressId);

        return "Address deleted successfully";
    }
}
