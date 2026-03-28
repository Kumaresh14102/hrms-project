package com.hrms.employee.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class EmployeeResponseDTO {

    private Long id;

    private String employeeCode;

    private String firstName;
    private String lastName;

    private String email;
    private String mobile;

    private String gender;
    private LocalDate dob;

    private Long departmentId;
    private Long designationId;
    private Long managerId;

    private LocalDate dateOfJoining;
    private LocalDate probationEndDate;

    private String employmentType;
    private String status;

    private String maritalStatus;
    private String bloodGroup;
    private String profilePhotoUrl;

    private LocalDateTime createdAt;
}
