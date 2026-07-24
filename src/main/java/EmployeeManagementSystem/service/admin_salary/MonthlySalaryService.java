package EmployeeManagementSystem.service.admin_salary;

import EmployeeManagementSystem.entity.Salary;

import java.util.List;

public interface MonthlySalaryService {
    Salary generateMonthlySalary(Long employeeId, String month, Integer year);

    List<Salary> generateMonthlySalaryForAllEmployees(String month, Integer year);

    Salary getSalaryByEmployeeAndMonth(Long employeeId, String month, Integer year);

    List<Salary> getSalariesByMonth(String month, Integer year);

    List<Salary> getSalariesByEmployee(Long employeeId);

    // Additional helper method for the slip view
    Salary getSalaryById(Long id);
}
