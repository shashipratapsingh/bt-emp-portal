package EmployeeManagementSystem.dto.dynamic;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OnboardingProjectsDTO {
    private BigDecimal avgRevenuePerProject;

    private long totalProjects;
    private long newProjects;             // added this month / year
    private BigDecimal netRevenue;
    private BigDecimal profit;
    private double margin;
}
