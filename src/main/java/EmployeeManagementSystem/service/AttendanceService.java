package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.Attendance;
import EmployeeManagementSystem.entity.AttendanceTracking;
import EmployeeManagementSystem.entity.WfhRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceService {

    Attendance saveAttendance(
            Attendance attendance);

    List<Attendance> getAllAttendance();

    Attendance getAttendanceById(
            Long id);

    void deleteAttendance(Long id);
    List<Attendance> getTodayAttendance();
    List<Attendance> getTodayWFHEmployees();
    List<AttendanceTracking> getAttendanceLogsByEmployeeId(String employeeId);
    void signoffEmployee(String employeeId);

}