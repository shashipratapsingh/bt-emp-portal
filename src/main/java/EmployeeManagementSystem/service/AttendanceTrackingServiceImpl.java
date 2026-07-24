package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.Attendance;
import EmployeeManagementSystem.entity.AttendanceTracking;
import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.repository.AttendanceTrackingRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceTrackingServiceImpl implements AttendanceTrackingService {

    private final AttendanceTrackingRepository attendanceTrackingRepository;


    @Override
    public void markLogin(Employee employee) {

        System.out.println("===== markLogin Called =====");
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String emp=authentication.getName();
        AttendanceTracking attendanceTracking = new AttendanceTracking();

        attendanceTracking.setEmployeeId(emp);
        attendanceTracking.setEmployeeName(employee.getFirstName());
        attendanceTracking.setDate(LocalDate.now());
        attendanceTracking.setLoginTime(LocalDateTime.now());
        attendanceTracking.setStatus("Present");

        attendanceTrackingRepository.save(attendanceTracking);

        System.out.println("===== Attendance Saved Successfully =====");
    }

    @Override
    public void markLogout(Long employeeId) {

        AttendanceTracking attendanceTracking =
                attendanceTrackingRepository.findTopByEmployeeIdAndDateOrderByIdDesc(
                        employeeId,
                        LocalDate.now()
                );

        if (attendanceTracking != null) {

            LocalDateTime logoutTime = LocalDateTime.now();
            attendanceTracking.setLogoutTime(logoutTime);

            Duration duration = Duration.between(
                    attendanceTracking.getLoginTime(),
                    logoutTime
            );

            double workingHours = duration.toMinutes() / 60.0;
            attendanceTracking.setWorkingHours(workingHours);

//            attendanceTrackingRepository.save(attendanceTracking);
            attendanceTrackingRepository.save(attendanceTracking);

            System.out.println("Saved ID = " + attendanceTracking.getId());
        }
    }

    @Override
    public AttendanceTracking getTodayAttendance(Long employeeId) {
        return attendanceTrackingRepository
                .findTopByEmployeeIdAndDateOrderByIdDesc(
                        employeeId,
                        LocalDate.now());
    }

//    @Override
//    public List<AttendanceTracking> getAttendanceHistory(Long employeeId) {
//        return attendanceTrackingRepository.findByEmployeeIdAndDateOrderByIdDesc(employeeId);
//    }
@Override
public List<AttendanceTracking> getAttendanceHistory(Long employeeId) {
    return attendanceTrackingRepository
            .findByEmployeeIdOrderByDateDesc(employeeId);
}
    @Override
    public LocalDateTime getPunchInTimeForToday(String employeeId) {
        // 1. डेटाबेस से आज का अटेंडेंस रिकॉर्ड खोजें
        Optional<Attendance> attendanceOpt = attendanceTrackingRepository.findTodayAttendanceByEmployeeId(employeeId);

        // 2. अगर रिकॉर्ड मौजूद है, तो punchInTime (LocalDateTime) रिटर्न करें
        if (attendanceOpt.isPresent()) {
            return attendanceOpt.get().getCheckInTime(); // मान लेते हैं एंटिटी में getPunchInTime() मेथड है
        }

        // 3. अगर कर्मचारी ने आज पंच-इन नहीं किया है, तो null रिटर्न करें
        return null;
    }

//    @Override
//    public List<AttendanceTracking> getAttendanceHistory(Long employeeId) {
//
//        return attendanceTrackingRepository
//                .findByEmployeeIdOrderByDateDesc(employeeId);
//    }
}