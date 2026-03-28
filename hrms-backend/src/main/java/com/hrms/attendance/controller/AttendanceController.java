package com.hrms.attendance.controller;

import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;


import org.springframework.web.bind.annotation.*;

import com.hrms.attendance.entity.*;
import com.hrms.employee.entity.*;
import com.hrms.employee.repository.*;
import com.hrms.attendance.service.*;
import com.hrms.attendance.dto.*;


@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:4200") // Add CORS support for Angular
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeRepository employeeRepository;

    public AttendanceController(
            AttendanceService attendanceService,
            EmployeeRepository employeeRepository) {
        this.attendanceService = attendanceService;
        this.employeeRepository = employeeRepository;
    }

    // CHECK-IN API
//    @PostMapping("/check-in/{employeeCode}")
//    public Attendance checkIn(@PathVariable String employeeCode,
//    		                  @RequestParam String workLocation) {
//
//        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
//                .orElseThrow(() -> new RuntimeException("Employee not found"));
//
//        return attendanceService.checkIn(employee, workLocation);
//    }
    @PostMapping("/check-in")
    public Attendance checkIn(@RequestParam String workLocation,
                              org.springframework.security.core.Authentication authentication) {

        String email = authentication.getName(); // gets logged-in email

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return attendanceService.checkIn(employee, workLocation);
    }
    
    // CHECK_OUT API
//    @PostMapping("/check-out/{employeeCode}")
//    public Attendance checkOut(@PathVariable String employeeCode) {
//    	Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
//    			.orElseThrow(() -> new RuntimeException("Employee not found" + employeeCode));
//        return attendanceService.checkOut(employee);
//    }
    @PostMapping("/check-out")
    public Attendance checkOut(
            org.springframework.security.core.Authentication authentication) {

        String email = authentication.getName();

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return attendanceService.checkOut(employee);
    }


    // GET attendance by employee
    @GetMapping("/{employeeCode}")
    public List<Attendance> getAttendance(@PathVariable String employeeCode) {

        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return attendanceService.getAttendance(employee);
    }
    
 // ONE DAY ATTENDANCE SUMMARY
    @GetMapping("/summary")
    public Map<String, Object> getDailySummary(
            @RequestParam String employeeCode,
            @RequestParam LocalDate date
    ) {
        Employee emp = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new RuntimeException("Employee not found"+ employeeCode));

        long minutes = attendanceService.calculateTotalMinutes(emp, date);
        String status = attendanceService.calculateDayStatus(emp, date);

        Map<String, Object> result = new HashMap<>();
        result.put("totalMinutes", minutes);
        result.put("totalHours", minutes / 60.0);
        result.put("status", status);

        return result;
    }

    
    @GetMapping("/monthly-summary")
    public Map<String, Object> getMonthlySummary(
            @RequestParam String employeeCode,
            @RequestParam int year,
            @RequestParam int month) {

        Employee emp = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new RuntimeException("Employee not found with code: " + employeeCode));

        return attendanceService.getMonthlySummary(emp, year, month);
    }
    
    // HR REPORT API
    @GetMapping("/hr-report")
    public List<Attendancedto> getHRReport(
    		@RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate) {

     return attendanceService.getHrReport(fromDate, toDate);
    }
    
    // HR DASHBOARD SUMMARY (ALL EMPLOYEES)
    @GetMapping("/dashboard-summary")
    public Map<String, Object> getDashboardSummary(
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {

        LocalDate start = null;
        LocalDate end = null;

        if (fromDate != null && toDate != null) {
            start = LocalDate.parse(fromDate);
            end = LocalDate.parse(toDate);
        }

        return attendanceService.getDashboardSummary(start, end);
    }

    // ADMIN/HR MANUAL CHECKOUT
    @PostMapping("/manual-checkout")
    public Attendance manualCheckout(
            @RequestParam String employeeCode,
            @RequestParam LocalDate date,
            @RequestParam java.time.LocalTime checkOutTime) {

        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return attendanceService.manualCheckout(employee, date, checkOutTime);
    }




}
