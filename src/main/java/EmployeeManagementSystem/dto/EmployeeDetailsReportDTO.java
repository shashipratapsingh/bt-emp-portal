// EmployeeDetailDTO.java
package EmployeeManagementSystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class EmployeeDetailsReportDTO {
    // Employee fields
    private Long id;
    private String fullName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private LocalDate joiningDate;
    private String workMode;
    private String departmentName;
    private Long departmentId;

    // Profile fields
    private String userId;
    private String profileId;
    private String gender;
    private String bloodGroup;
    private String maritalStatus;
    private String fieldOfStudy;
    private String highestQualification;
    private String university;
    private String passingYear;
    private String bankName;
    private String accountNumber;
    private String accountHolderName;
    private String ifscCode;
    private String currentAddress;
    private String permanentAddress;
    private String photo;
    private String status;
    private String designation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Salary fields (latest)
    private Double basicSalary;
    private Double hra;
    private Double allowance;
    private Double bonus;
    private Double deductions;
    private Double grossSalary;
    private Double netSalary;
    private String paymentStatus;
    private String month;
    private Integer year;
    private LocalDate paymentDate;

    // Salary history
    private List<SalarySummaryDTO> salaryHistory;
}