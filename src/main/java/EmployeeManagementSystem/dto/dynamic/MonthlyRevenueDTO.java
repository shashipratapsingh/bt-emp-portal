package EmployeeManagementSystem.dto.dynamic;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MonthlyRevenueDTO {
    private int year;
    private int month;
    private BigDecimal totalRevenue;
    private double percentageChange;      // vs previous month
    private List<BigDecimal> dailyRevenue; // optional
}
