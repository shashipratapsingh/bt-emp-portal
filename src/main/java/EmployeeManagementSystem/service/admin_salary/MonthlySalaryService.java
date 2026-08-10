package EmployeeManagementSystem.service.admin_salary;

import EmployeeManagementSystem.entity.Salary;

import java.util.List;

public interface MonthlySalaryService {

    Salary generateMonthlySalary(Long profileId, String month, Integer year);

    List<Salary> generateMonthlySalaryForAllEmployees(String month, Integer year);

    Salary getSalaryByEmployeeProfileAndMonth(Long profileId, String month, Integer year);

    List<Salary> getSalariesByMonth(String month, Integer year);

    List<Salary> getSalariesByEmployeeProfile(Long profileId);

    Salary getSalary(Long id);
}