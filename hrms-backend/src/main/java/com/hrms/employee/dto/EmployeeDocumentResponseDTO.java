package com.hrms.employee.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmployeeDocumentResponseDTO {

    private Long id;
    private String documentType;
    private String filePath;
    private LocalDateTime uploadedAt;
}
