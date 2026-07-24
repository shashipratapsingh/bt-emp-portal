package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.LoginRequest;
import EmployeeManagementSystem.entity.RegisterEmployee;

import java.util.List;

public interface RegisterEmployeeService {
    String registerEmployee(RegisterEmployee employee);
    String login(LoginRequest request);
    String generateNextEmployeeId();
    void sendOtpProcessing(String email);
    void verifyOtpAndChangePassword(String email,String otp,String newPassword);
    RegisterEmployee getEmployeeById(String employeeId);
    List<RegisterEmployee> getUpcomingBirthdays();
}
