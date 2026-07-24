//package EmployeeManagementSystem.repository;
//
//import EmployeeManagementSystem.entity.LeaveRequest;
//import EmployeeManagementSystem.enums.LeaveStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface LeaveRepository extends JpaRepository<LeaveRequest,Long> {
//    List<LeaveRequest> findByEmployeeId(String employeeId);
//    long countByStatus(LeaveStatus status);
//}




package EmployeeManagementSystem.repository;

import EmployeeManagementSystem.entity.LeaveRequest;
import EmployeeManagementSystem.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveRequest, Long> {

    // Get employee leave history
    List<LeaveRequest> findByEmployeeId(String employeeId);


    // Count leaves by status (APPROVED, PENDING, REJECTED)
    long countByStatus(LeaveStatus status);


    // Get employee leaves by status
    List<LeaveRequest> findByEmployeeIdAndStatus(
            String employeeId,
            LeaveStatus status
    );


    // Calendar: Get employee leaves overlapping selected date range
    // Example: Month calendar load
    List<LeaveRequest> findByEmployeeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String employeeId,
            LocalDate calendarEndDate,
            LocalDate calendarStartDate
    );


    // Calendar/Admin: Get all leaves between dates
    List<LeaveRequest> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(
            LocalDate calendarEndDate,
            LocalDate calendarStartDate
    );
}