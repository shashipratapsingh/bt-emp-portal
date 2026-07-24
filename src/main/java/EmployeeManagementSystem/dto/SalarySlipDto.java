package EmployeeManagementSystem.dto;

import lombok.Data;

@Data
public class SalarySlipDto {

    private String employeeName;
    private String employeeId;
    private String designation;
    private Double basicSalary;
    private Double bonus;
    private Double deduction;
    private Double netSalary;
    private String monthYear;
}
