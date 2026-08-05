package EmployeeManagementSystem.dto.dynamic;

public class ProjectTypeDTO {
    private double revenue;
    private double profit;
    private double loss;

    public ProjectTypeDTO(double revenue, double profit, double loss) {
        this.revenue = revenue;
        this.profit = profit;
        this.loss = loss;
    }

    public double getRevenue() { return revenue; }
    public double getProfit() { return profit; }
    public double getLoss() { return loss; }
}