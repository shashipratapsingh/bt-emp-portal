package EmployeeManagementSystem.dto;

import lombok.Data;

@Data
public class LoginRequest {
    public String userId;
    public String password;
    private String workMode;
}
