package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.LeaveRequest;
import EmployeeManagementSystem.entity.admin_leave.LeaveType;

import java.util.List;

public interface LeaveService {

    List<LeaveRequest> getAllLeaves();

    List<LeaveRequest> getLeavesByEmployeeId(String employeeId);
    LeaveRequest applyLeave(LeaveRequest leaveRequest);
    List<LeaveRequest> getAllLeaveRequest();
    List<LeaveRequest> getTodayAllLeaveRequest();
    void updateleaveStatus(Long id,String action);

    List<LeaveType> getActiveLeaveTypes();





}
