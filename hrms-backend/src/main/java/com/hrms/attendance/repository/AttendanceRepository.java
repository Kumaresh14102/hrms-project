package com.hrms.attendance.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hrms.attendance.entity.*;

import java.util.List;

import com.hrms.employee.entity.*;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Custom query: get attendance by employee
    List<Attendance> findByEmployee(Employee employee);
    List<Attendance> findByEmployeeAndDate(Employee employee, LocalDate date);

    Optional<Attendance> findTopByEmployeeAndDateOrderBySessionNoDesc(
    		Employee employee, LocalDate date);
    Optional<Attendance> findByEmployeeAndDateAndCheckOutIsNull(
            Employee employee, LocalDate date);
    List<Attendance> findByEmployeeAndDateBetween(
            Employee employee, LocalDate startDate, LocalDate endDate);
    List<Attendance> findByDateBetween(LocalDate fromDate, LocalDate toDate);

    Optional<Attendance> 
    findTopByEmployeeAndCheckOutIsNullOrderByDateDesc(Employee employee);



}
