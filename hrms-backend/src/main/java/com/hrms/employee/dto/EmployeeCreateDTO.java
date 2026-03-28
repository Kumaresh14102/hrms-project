package com.hrms.employee.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeCreateDTO {

    private String employeeCode;
    private String firstName;
    private String lastName;

    private String gender;
    private LocalDate dob;

    private String email;
    private String mobile;

    private Long departmentId;
    private Long designationId;
    private Long managerId;

    private LocalDate dateOfJoining;
    private String employmentType;

    private String maritalStatus;
    private String bloodGroup;
    private String profilePhotoUrl;

    private LocalDate probationEndDate;
}
