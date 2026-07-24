package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.Attendance;
import EmployeeManagementSystem.entity.AttendanceTracking;
import EmployeeManagementSystem.entity.Employee;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceTrackingService {
    void markLogin (Employee employee);
    void markLogout (Long employeeId);

    AttendanceTracking getTodayAttendance(Long employeeId);

    List<AttendanceTracking> getAttendanceHistory(Long employeeId);
    LocalDateTime getPunchInTimeForToday(String employeeId);

}
