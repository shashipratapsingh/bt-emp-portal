// EmployeeReportDTO.java
package EmployeeManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeReportDTO {
    private Long id;
    private String userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String departmentName;
    private String designation;
    private String currentProject;
    private BigDecimal basicSalary;
    private BigDecimal netSalary;
    private String salaryStatus; // PAID, UNPAID, PENDING
    private String employeeStatus; // ACTIVE, INACTIVE, DELETED
    private String photo;
    private String profileId;
}