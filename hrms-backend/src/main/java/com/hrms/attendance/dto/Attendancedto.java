package com.hrms.attendance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Attendancedto {
    private String date;          // Attendance date
    private String employeeName;  // Employee full name
    private double workingHours;  // Calculated: checkOut - checkIn
    private String lateMark;      // "Yes"/"No" based on official start time
    private double overtime;      // Calculated if workingHours > 8 hrs
}
