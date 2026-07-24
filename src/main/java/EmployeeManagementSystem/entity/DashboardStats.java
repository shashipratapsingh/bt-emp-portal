package com.hrms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private Integer totalEmployees;
    private Double totalPayrollCost;
    private Double paidSalary;
    private Double pendingSalary;
    private Double activeEmployeeGrowth; // percentage
}