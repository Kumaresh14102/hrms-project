package com.hrms.employee.dto;

import lombok.Data;

@Data
public class EmployeeEmergencyContactCreateDTO {

    private Long employeeId;
    private String name;
    private String relation;
    private String phone;
}
