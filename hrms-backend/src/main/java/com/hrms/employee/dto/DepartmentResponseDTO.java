package com.hrms.employee.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentResponseDTO {

    private Long id;
    private String name;
    private Long parentId;
}
