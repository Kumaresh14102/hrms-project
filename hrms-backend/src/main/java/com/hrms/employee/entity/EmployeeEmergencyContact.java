package com.hrms.employee.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_emergency_contacts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEmergencyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String relation;

    @Column(nullable = false, length = 15)
    private String phone;
}
