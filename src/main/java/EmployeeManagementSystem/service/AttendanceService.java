package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.AttendanceRecordDTO;
import EmployeeManagementSystem.entity.Attendance;
import EmployeeManagementSystem.entity.AttendanceTracking;
import EmployeeManagementSystem.entity.WfhRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
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
    Page<AttendanceRecordDTO> getFilteredAttendance(LocalDate fromDate, LocalDate toDate,
                                                    String department, String status,
                                                    String keyword, Pageable pageable);
    AttendanceSummary getAttendanceSummary(LocalDate date, String department);

}