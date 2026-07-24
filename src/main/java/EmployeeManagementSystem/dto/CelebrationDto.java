package EmployeeManagementSystem.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CelebrationDto {
    private String userId;
    private String fullName;
    private LocalDateTime registeredAt;
    private LocalDate dob;
    private String type;       // Birthday / Anniversary
    private String photo;
}
