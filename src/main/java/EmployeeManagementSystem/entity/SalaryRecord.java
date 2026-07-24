package EmployeeManagementSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryRecord{
    private String employeeId;
    private String employeeName;
    private String department;
    private String designation;
    private Double grossSalary;
    private Double deductions;
    private Double netSalary;
    private String paymentStatus;
}
