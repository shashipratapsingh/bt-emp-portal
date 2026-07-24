package EmployeeManagementSystem.repository;

import EmployeeManagementSystem.entity.Attendance;
import EmployeeManagementSystem.entity.AttendanceTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceTrackingRepository
        extends JpaRepository<AttendanceTracking, Long> {

    AttendanceTracking findTopByEmployeeIdAndDateOrderByIdDesc(
            Long employeeId,
            LocalDate date
    );

//    List<AttendanceTracking> findByEmployeeIdAndDateOrderByIdDesc(Long employeeId);

    List<AttendanceTracking> findByEmployeeIdOrderByDateDesc(Long employeeId);
    //@Query("SELECT a FROM Attendance a WHERE a.employeeId = :empId AND FUNCTION('DATE', a.punchInTime) = CURRENT_DATE")
    Optional<Attendance> findTodayAttendanceByEmployeeId(@Param("empId") String empId);
    //List<AttendanceTracking> findByEmployeeId(String employeeId);
    List<AttendanceTracking> findByEmployeeIdOrderByDateDescLoginTimeAsc(String employeeId);
    Optional<AttendanceTracking> findByEmployeeIdAndDate(String employeeId,
                                                         LocalDate date);
    AttendanceTracking findTopByEmployeeIdAndDateOrderByLoginTimeDesc(String employeeId, LocalDate date);
}