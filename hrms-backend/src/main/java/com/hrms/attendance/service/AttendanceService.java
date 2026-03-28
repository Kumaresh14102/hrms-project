package com.hrms.attendance.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hrms.attendance.entity.*;
import com.hrms.employee.entity.*;
import com.hrms.attendance.repository.*;

import java.time.Duration;

import com.hrms.attendance.dto.*;
import java.util.stream.Collectors;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;



@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }
    
    // CHECK-IN
    public Attendance checkIn(Employee employee, String workLocation) {

        // LocalDate today = LocalDate.now();

    	// Auto close previous open attendance
        attendanceRepository
            .findTopByEmployeeAndCheckOutIsNullOrderByDateDesc(employee)
            .ifPresent(a -> {
                if (!a.getDate().equals(LocalDate.now())) {
                    a.setCheckOut(LocalTime.of(18, 0)); // Auto close 6 PM
                    a.setStatus("COMPLETED");
                    attendanceRepository.save(a);
                }
            });

        attendanceRepository
        .findByEmployeeAndDateAndCheckOutIsNull(employee, LocalDate.now())
        .ifPresent(a -> {
            throw new RuntimeException("Already checked in. Please check out first.");
        });
        
        int nextSession = attendanceRepository
                .findTopByEmployeeAndDateOrderBySessionNoDesc(employee, LocalDate.now())
                .map(a -> a.getSessionNo() + 1)
                .orElse(1);
        
        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setDate(LocalDate.now());
        attendance.setCheckIn(LocalTime.now());
        attendance.setSessionNo(nextSession);
        attendance.setStatus("PRESENT");
        attendance.setWorkLocation(workLocation);

        return attendanceRepository.save(attendance);
    }
    
    // CHECK_OUT
    public Attendance checkOut(Employee employee) {

        Attendance attendance = attendanceRepository
            .findByEmployeeAndDateAndCheckOutIsNull(employee, LocalDate.now())
            .orElseThrow(() ->
                new RuntimeException("No check-in found for today")
            );

        if (attendance.getCheckOut() != null) {
            throw new RuntimeException("Already checked out");
        }

        attendance.setCheckOut(LocalTime.now());
        attendance.setStatus("COMPLETED");

        return attendanceRepository.save(attendance);
    }

    

    // GET attendance by employee
    public List<Attendance> getAttendance(Employee employee) {
        return attendanceRepository.findByEmployee(employee);
    }
    
    //ONE DAY WORKING TIME
    public long calculateTotalMinutes(Employee employee, LocalDate date) {

        List<Attendance> sessions =
                attendanceRepository.findByEmployeeAndDate(employee, date);

        long totalMinutes = 0;

        for (Attendance a : sessions) {
            if (a.getCheckIn() != null && a.getCheckOut() != null) {
                totalMinutes +=
                    java.time.Duration.between(
                        a.getCheckIn(),
                        a.getCheckOut()
                    ).toMinutes();
            }
        }

        return totalMinutes;
    }
      
    public String calculateDayStatus(Employee employee, LocalDate date) {

        long minutes = calculateTotalMinutes(employee, date);

        if (minutes >= 480) {          // 8 hours
            return "FULL_DAY";
        } else if (minutes >= 240) {   // 4 hours
            return "HALF_DAY";
        } else {
            return "ABSENT";
        }
    }
    
    public Map<String, Object> getMonthlySummary(
            Employee employee,
            int year,
            int month) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Attendance> list =
                attendanceRepository.findByEmployeeAndDateBetween(
                        employee, start, end);

        long totalMinutes = 0;
        int fullDays = 0;
        int halfDays = 0;

        Map<LocalDate, Long> dailyMinutes = new HashMap<>();

        for (Attendance a : list) {
            if (a.getCheckIn() != null && a.getCheckOut() != null) {
                long minutes =
                    Duration.between(a.getCheckIn(), a.getCheckOut()).toMinutes();
                dailyMinutes.merge(a.getDate(), minutes, Long::sum);
            }
        }

        for (long minutes : dailyMinutes.values()) {
            totalMinutes += minutes;
            if (minutes >= 480) fullDays++;
            else if (minutes >= 240) halfDays++;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("fullDays", fullDays);
        result.put("halfDays", halfDays);
        result.put("absentDays",
                start.lengthOfMonth() - (fullDays + halfDays));
        result.put("totalHours", totalMinutes / 60.0);

        return result;
    }


    
//    public Attendance save(Attendance attendance) {
//
//    	attendanceRepository.findByEmployeeIdAndDate(
//    			attendance.getEmployee().getId(),
//                attendance.getDate()
//        ).ifPresent(a -> {
//            throw new RuntimeException(
//                "Attendance already marked for today"
//            );
//        });
//
//        return attendanceRepository.save(attendance);
//    }
    
    
    public List<Attendancedto> getHrReport(LocalDate fromDate, LocalDate toDate) {

        List<Attendance> allAttendance;

        if (fromDate != null && toDate != null) {
            allAttendance = attendanceRepository
                    .findByDateBetween(fromDate, toDate);
        } else {
            allAttendance = attendanceRepository.findAll();
        }

        Map<String, List<Attendance>> grouped =
                allAttendance.stream()
                        .collect(Collectors.groupingBy(a ->
                                a.getEmployee().getEmployeeCode() + "_" + a.getDate()
                        ));

        return grouped.values().stream().map(list -> {

            Attendance first = list.get(0);
            Employee emp = first.getEmployee();
            LocalDate date = first.getDate();

            long totalMinutes = 0;
            boolean isLate = false;

            for (Attendance a : list) {
                if (a.getCheckIn() != null && a.getCheckOut() != null) {

                    totalMinutes +=
                            Duration.between(a.getCheckIn(), a.getCheckOut())
                                    .toMinutes();

                    if (a.getCheckIn().isAfter(LocalTime.of(9, 30))) {
                        isLate = true;
                    }
                }
            }

            double hours = totalMinutes / 60.0;
            double overtime = hours > 8 ? hours - 8 : 0;

            Attendancedto dto = new Attendancedto();
            dto.setDate(date.toString());
            dto.setEmployeeName(emp.getFirstName() + " " + emp.getLastName());
            dto.setWorkingHours(hours);
            dto.setLateMark(isLate ? "YES" : "NO");
            dto.setOvertime(overtime);

            return dto;

        }).collect(Collectors.toList());
    }

    // =========================================
    // HR DASHBOARD SUMMARY (ALL EMPLOYEES)
    // =========================================
    public Map<String, Object> getDashboardSummary(
            LocalDate fromDate,
            LocalDate toDate) {

        List<Attendance> records;

        if (fromDate != null && toDate != null) {
            records = attendanceRepository.findByDateBetween(fromDate, toDate);
        } else {
            records = attendanceRepository.findAll();
        }

        long totalEmployees = records.stream()
                .map(a -> a.getEmployee().getEmployeeCode())
                .distinct()
                .count();

        long presentDays = records.stream()
                .filter(a -> "COMPLETED".equals(a.getStatus()))
                .count();

        long absentDays = records.stream()
                .filter(a -> "ABSENT".equals(a.getStatus()))
                .count();

        double totalOvertime = records.stream()
                .mapToDouble(a -> {
                    if (a.getCheckIn() != null && a.getCheckOut() != null) {
                        long minutes = Duration.between(
                                a.getCheckIn(),
                                a.getCheckOut()
                        ).toMinutes();

                        double hours = minutes / 60.0;
                        return hours > 8 ? hours - 8 : 0;
                    }
                    return 0;
                }).sum();

        Map<String, Object> result = new HashMap<>();
        result.put("totalEmployees", totalEmployees);
        result.put("presentDays", presentDays);
        result.put("absentDays", absentDays);
        result.put("overtimeHours", totalOvertime);

        return result;
    }

    // MANUAL CHECKOUT (ADMIN USE)
    public Attendance manualCheckout(Employee employee, LocalDate date, LocalTime checkOutTime) {
        
        Attendance attendance = attendanceRepository
                .findTopByEmployeeAndDateOrderBySessionNoDesc(employee, date)
                .orElseThrow(() ->
                        new RuntimeException("No attendance found for this date")
                );
        
        // If already checked out
//        if (attendance.getCheckOut() != null) {
//        	System.out.println("Already checked out attempt!");
//        	throw new ResponseStatusException(
//        	        HttpStatus.BAD_REQUEST,
//        	        "Employee already checked out for this date"
//        	);
//        }

     // Safety: if check-in not present
        if (attendance.getCheckIn() == null) {
            throw new RuntimeException("Check-in missing. Cannot checkout.");
        }

        // 🚨 Important: Prevent invalid time
        if (checkOutTime.isBefore(attendance.getCheckIn())) {
            throw new RuntimeException("Checkout time cannot be before check-in time");
        }

        boolean isUpdated = attendance.getCheckOut() != null;
        
        // Set manual checkout time
        attendance.setCheckOut(checkOutTime);
        
        System.out.println("Already checked out attempt!");

        // Optional: calculate duration properly
        long minutesWorked = Duration
                .between(attendance.getCheckIn(), checkOutTime)
                .toMinutes();

        if (minutesWorked >= 480) {
            attendance.setStatus("PRESENT");
        } else if (minutesWorked >= 240) {
            attendance.setStatus("HALF_DAY");
        } else {
            attendance.setStatus("SHORT_HOURS");
        }
        if (isUpdated) {
            attendance.setStatus(attendance.getStatus() + "_UPDATED_BY_ADMIN");
        }
        return attendanceRepository.save(attendance);
    }





}
