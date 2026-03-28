package com.hrms.attendance.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.hrms.employee.entity.Employee;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attendance")
@Getter
@Setter
@NoArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many attendance records for one employee
    @ManyToOne
    @JoinColumn(name = "employee_code", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate date;
    
    private LocalTime checkIn;
    private LocalTime checkOut;
    private Integer sessionNo; //NEW
    private String status; // PRESENT, ABSENT, HALF_DAY
    
    @Column(name = "work_location")
    private String workLocation;
}
