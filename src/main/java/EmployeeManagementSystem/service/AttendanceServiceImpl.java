package EmployeeManagementSystem.service;


import EmployeeManagementSystem.dto.AttendanceRecordDTO;
import EmployeeManagementSystem.entity.Attendance;
import EmployeeManagementSystem.entity.AttendanceTracking;
import EmployeeManagementSystem.enums.AttendanceStatus;
import EmployeeManagementSystem.enums.WorkMode;
import EmployeeManagementSystem.repository.AttendanceRepository;
import EmployeeManagementSystem.repository.AttendanceTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    //just for trail
    private final AttendanceRepository attendanceRepository;
    private final AttendanceTrackingRepository attendanceTrackingRepository;


    @Override
    public Attendance saveAttendance(Attendance attendance) {

        return attendanceRepository.save(
                attendance);
    }

    @Override
    public List<Attendance> getAllAttendance() {

        return attendanceRepository.findAll();
    }

    @Override
    public Attendance getAttendanceById(
            Long id) {

        return attendanceRepository.findById(id)
                .orElse(null);
    }

    @Override
    public void deleteAttendance(Long id) {

        attendanceRepository.deleteById(id);
    }

    public List<Attendance> getTodayAttendance() {
        return attendanceRepository.findByAttendanceDate(LocalDate.now());
    }

    public List<Attendance> getTodayWFHEmployees() {

        return attendanceRepository
                .findByAttendanceDateAndWorkMode(
                        LocalDate.now(),
                        WorkMode.WFH
                );

    }

    public List<AttendanceTracking> getAttendanceLogsByEmployeeId(String employeeId) {
        List<AttendanceTracking> logs = attendanceTrackingRepository.findByEmployeeIdOrderByDateDescLoginTimeAsc(employeeId);

        for (AttendanceTracking log : logs) {
            if (log.getLoginTime() != null && log.getLogoutTime() != null) {

                Duration duration = Duration.between(log.getLoginTime(), log.getLogoutTime());

                double hours = duration.toMinutes() / 60.0;
                hours = Math.round(hours * 100.0) / 100.0;

                log.setWorkingHours(hours);
            }

        }

        return logs;
    }
    public void signoffEmployee(String employeeId) {
        LocalDate today = LocalDate.now();

        // 1. Aaj ka sabse latest record nikalen (Login Time ke hisab se Descending)
        Optional<AttendanceTracking> latestLogOpt = attendanceTrackingRepository
                .findTopByEmployeeIdOrderByLoginTimeDesc(employeeId);

        if (latestLogOpt.isPresent()) {
            AttendanceTracking log = latestLogOpt.get();

            // Check karein ki latest log aaj ka hi hai ya nahi
            if (log.getLoginTime() != null && log.getLoginTime().toLocalDate().equals(today)) {

                // 2. Logout time ko naye/current time se OVERWRITE karein
                LocalDateTime newLogoutTime = LocalDateTime.now();
                log.setLogoutTime(newLogoutTime);

                // 3. Naye Logout time ke hisaab se Duration RE-CALCULATE karein
                Duration duration = Duration.between(log.getLoginTime(), newLogoutTime);
                double updatedHours = duration.toMinutes() / 60.0;
                log.setWorkingHours(updatedHours);

                // 4. Database mein UPDATED record SAVE karein
                attendanceTrackingRepository.save(log);
            }
        }
    }

    @Override
    public Page<AttendanceRecordDTO> getFilteredAttendance(LocalDate fromDate, LocalDate toDate, String department, String status, String keyword, Pageable pageable) {
        // Convert status string to enum (ignore if null/empty)
        AttendanceStatus statusEnum = null;
        if (status != null && !status.isEmpty()) {
            try {
                statusEnum = AttendanceStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        Page<Attendance> page = attendanceRepository.findFiltered(fromDate, toDate, department, statusEnum, keyword, pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public AttendanceSummary getAttendanceSummary(LocalDate date, String department) {
        long total = attendanceRepository.countTotalEmployeesByDepartment(department);
        long present = attendanceRepository.countByDateAndStatusAndDepartment(date, AttendanceStatus.PRESENT, department);
        long absent = attendanceRepository.countByDateAndStatusAndDepartment(date, AttendanceStatus.ABSENT, department);
        long late = attendanceRepository.countByDateAndStatusAndDepartment(date, AttendanceStatus.LATE, department);
        long onLeave = attendanceRepository.countByDateAndStatusAndDepartment(date, AttendanceStatus.LEAVE, department);

        int percentage = total > 0 ? (int) ((present + late) * 100 / total) : 0;

        AttendanceSummary summary = new AttendanceSummary();
        summary.setTotalEmployees(total);
        summary.setPresent(present);
        summary.setAbsent(absent);
        summary.setLate(late);
        summary.setOnLeave(onLeave);
        summary.setAttendancePercentage(percentage);
        return summary;
    }
    private AttendanceRecordDTO convertToDTO(Attendance attendance) {
        AttendanceRecordDTO dto = new AttendanceRecordDTO();
        dto.setEmployee(attendance.getEmployee());
        dto.setDate(attendance.getDate());
        dto.setCheckInTime(attendance.getCheckInTime());
        dto.setCheckOutTime(attendance.getCheckOutTime());
        dto.setWorkingHours(attendance.getWorkingHours());
        dto.setStatus(attendance.getStatus());  // enum kept
        return dto;
    }



}
