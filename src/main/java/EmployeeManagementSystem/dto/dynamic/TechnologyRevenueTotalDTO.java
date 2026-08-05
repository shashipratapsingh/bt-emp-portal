package EmployeeManagementSystem.dto.dynamic;

public class TechnologyRevenueTotalDTO {
    private int projects;
    private double revenue;
    private double profit;
    private double loss;
    private double margin;

    public TechnologyRevenueTotalDTO(int projects, double revenue, double profit, double loss, double margin) {
        this.projects = projects;
        this.revenue = revenue;
        this.profit = profit;
        this.loss = loss;
        this.margin = margin;
    }

    // Getters
    public int getProjects() { return projects; }
    public double getRevenue() { return revenue; }
    public double getProfit() { return profit; }
    public double getLoss() { return loss; }
    public double getMargin() { return margin; }
}