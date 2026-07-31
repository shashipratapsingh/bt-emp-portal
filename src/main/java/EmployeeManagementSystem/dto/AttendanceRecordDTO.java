package EmployeeManagementSystem.dto;

import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.enums.AttendanceStatus;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class AttendanceRecordDTO {
    private Employee employee;               // full employee object (or extract fields)
    private LocalDate date;
    private LocalDateTime checkInTime;
    private LocalTime checkOutTime;
    private Double workingHours;
    private AttendanceStatus status;          // keep enum for easier badge mapping
}