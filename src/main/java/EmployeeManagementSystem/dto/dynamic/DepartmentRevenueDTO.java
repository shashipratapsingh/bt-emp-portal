package EmployeeManagementSystem.dto.dynamic;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepartmentRevenueDTO {
    private String department;
    private BigDecimal monthlyRevenue;
    private BigDecimal yearlyRevenue;
    private double monthlyGrowth;
    private double yearlyGrowth;
    private String color;
}
