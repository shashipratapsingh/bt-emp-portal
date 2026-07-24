package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.LeaveRequest;

import java.util.List;

public interface LeaveService {

    List<LeaveRequest> getAllLeaves();

    List<LeaveRequest> getLeavesByEmployeeId(String employeeId);
    LeaveRequest applyLeave(LeaveRequest leaveRequest);
    List<LeaveRequest> getAllLeaveRequest();
    void updateleaveStatus(Long id,String action);



}
