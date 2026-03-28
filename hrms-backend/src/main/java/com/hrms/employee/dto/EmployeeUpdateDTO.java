package com.hrms.employee.dto;

import lombok.Data;

@Data
public class EmployeeUpdateDTO {

    private String firstName;
    private String lastName;

    private String mobile;
    private String departmentId;
    private String designationId;

    private String maritalStatus;
    private String bloodGroup;
    private String profilePhotoUrl;

    private String status;
}
