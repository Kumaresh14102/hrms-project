package com.hrms.employee.dto;

import lombok.Data;

@Data
public class EmployeeDocumentCreateDTO {

    private Long employeeId;
    private String documentType;
    private String filePath;
}
