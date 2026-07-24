package EmployeeManagementSystem.repository.admin_leave;

import EmployeeManagementSystem.entity.LeaveRequest;
import EmployeeManagementSystem.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// LeaveRequestRepository.java
@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByStatus(LeaveStatus status);
    List<LeaveRequest> findByEmployeeId(String employeeId);
    List<LeaveRequest> findByEmployeeIdAndStatus(String employeeId, LeaveStatus status);

    // Check overlapping leave for a given employee and date range
    boolean existsByEmployeeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String employeeId, LocalDate endDate, LocalDate startDate);
    // or using JPQL for more precision
}
