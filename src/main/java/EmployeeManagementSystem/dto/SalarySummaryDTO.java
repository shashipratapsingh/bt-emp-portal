// SalarySummaryDTO.java
package EmployeeManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalarySummaryDTO {
    private Long id;
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
}