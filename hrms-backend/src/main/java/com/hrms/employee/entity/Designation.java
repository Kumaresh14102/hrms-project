package com.hrms.employee.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "designations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer level;
}
