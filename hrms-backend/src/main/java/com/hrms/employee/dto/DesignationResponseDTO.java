package com.hrms.employee.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DesignationResponseDTO {

    private Long id;
    private String name;
    private Integer level;
}
