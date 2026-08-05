package EmployeeManagementSystem.dto.dynamic;

import java.math.BigDecimal;

public class ProjectLossDTO {
    private BigDecimal totalLoss;
    private BigDecimal highestLossAmount;
    private String highestLossProject;
    private BigDecimal lowestLossAmount;
    private String lowestLossProject;

    // Getters and setters
    public BigDecimal getTotalLoss() { return totalLoss; }
    public void setTotalLoss(BigDecimal totalLoss) { this.totalLoss = totalLoss; }

    public BigDecimal getHighestLossAmount() { return highestLossAmount; }
    public void setHighestLossAmount(BigDecimal highestLossAmount) { this.highestLossAmount = highestLossAmount; }

    public String getHighestLossProject() { return highestLossProject; }
    public void setHighestLossProject(String highestLossProject) { this.highestLossProject = highestLossProject; }

    public BigDecimal getLowestLossAmount() { return lowestLossAmount; }
    public void setLowestLossAmount(BigDecimal lowestLossAmount) { this.lowestLossAmount = lowestLossAmount; }

    public String getLowestLossProject() { return lowestLossProject; }
    public void setLowestLossProject(String lowestLossProject) { this.lowestLossProject = lowestLossProject; }
}