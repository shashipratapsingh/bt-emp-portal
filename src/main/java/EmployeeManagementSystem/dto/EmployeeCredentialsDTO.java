package EmployeeManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCredentialsDTO {
    private String userId;
    private String password;
    private String email;
    private String fullName;
}