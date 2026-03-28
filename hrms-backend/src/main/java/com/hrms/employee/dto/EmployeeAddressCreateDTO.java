package com.hrms.employee.dto;

import lombok.Data;

@Data
public class EmployeeAddressCreateDTO {

    private Long employeeId;

    private String type;

    private String line1;
    private String line2;

    private String city;
    private String state;
    private String pincode;
    private String country;
}
