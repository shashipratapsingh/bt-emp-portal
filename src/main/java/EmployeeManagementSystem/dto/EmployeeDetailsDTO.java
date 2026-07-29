package EmployeeManagementSystem.dto;


import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.EmployeeProfile;
import EmployeeManagementSystem.entity.Project;
import EmployeeManagementSystem.entity.Salary;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeDetailsDTO {
    private EmployeeProfile profile;
    private Employee employee;
    private List<Salary> salaries;
    private List<Project> projects;

    // convenience methods for total salary / count
    public Double getTotalNetSalary() {
        if (salaries == null) return 0.0;
        return salaries.stream().mapToDouble(Salary::getNetSalary).sum();
    }

    public Long getProjectCount() {
        return projects != null ? (long) projects.size() : 0L;
    }
    public Long getSalaryRecords() {
        return salaries != null ? (long) salaries.size() : 0L;
    }
    public Double getAverageNetSalary() {
        if (salaries == null || salaries.isEmpty()) return 0.0;
        return getTotalNetSalary() / salaries.size();
    }

    public Double getMinNetSalary() {
        if (salaries == null || salaries.isEmpty()) return 0.0;
        return salaries.stream().mapToDouble(Salary::getNetSalary).min().orElse(0.0);
    }

    public Double getMaxNetSalary() {
        if (salaries == null || salaries.isEmpty()) return 0.0;
        return salaries.stream().mapToDouble(Salary::getNetSalary).max().orElse(0.0);
    }
}