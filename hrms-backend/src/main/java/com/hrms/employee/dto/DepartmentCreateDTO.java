package com.hrms.employee.dto;

import lombok.Data;

@Data
public class DepartmentCreateDTO {

    private String name;
    private Long parentId; 
}
