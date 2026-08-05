package EmployeeManagementSystem.dto.dynamic;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class YearlyRevenueDTO {
    private int year;
    private BigDecimal totalRevenue;
    private double percentageChange;      // vs previous year
    private List<BigDecimal> monthlyRevenue; // 12 values
    // getters, setters
}
