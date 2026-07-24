package EmployeeManagementSystem.service.admin_leave;

import EmployeeManagementSystem.dto.LeaveBalanceAdjustDTO;
import EmployeeManagementSystem.dto.LeaveRequestDTO;
import EmployeeManagementSystem.entity.Holiday;
import EmployeeManagementSystem.entity.LeaveRequest;
import EmployeeManagementSystem.entity.admin_leave.*;
import java.util.List;

/**
 * Service interface for Leave Management operations.
 * Defines all admin and employee leave-related business logic.
 */
public interface AdminLeaveService {

    // ----- Leave Types -----
    List<LeaveType> findAllLeaveTypes();
    LeaveType saveLeaveType(LeaveType type);
    void deleteLeaveType(Long id);

    // ----- Leave Policies -----
    List<LeavePolicy> findAllPolicies();
    LeavePolicy savePolicy(LeavePolicy policy);

    // ----- Holidays -----
    List<Holiday> findAllHolidays();
    Holiday saveHoliday(Holiday holiday);
    void deleteHoliday(Long id);

    // ----- Leave Requests (Employee apply) -----
    LeaveRequest applyForLeave(LeaveRequestDTO dto);

    // ----- Admin Approve / Reject -----
    LeaveRequest approveLeave(Long requestId, String comment);
    LeaveRequest rejectLeave(Long requestId, String comment);

    // ----- Manual Balance Adjustment (Super Admin) -----
    void adjustLeaveBalance(LeaveBalanceAdjustDTO dto);

    // ----- List requests by status -----
    List<LeaveRequest> findRequestsByStatus(String status);


    // ------------------- Get employee balances -------------------
    List<LeaveBalance> getEmployeeBalances(String employeeId);
}