//package EmployeeManagementSystem.service;
//
//import EmployeeManagementSystem.dto.SalarySlipDto;
//import EmployeeManagementSystem.entity.Salary;
//import org.jspecify.annotations.Nullable;
//
//import java.util.List;
//
//public interface SalaryService {
//
//    Salary saveSalary(Salary salary);
//
//    Salary getSalaryById(Long id);
//
//    void deleteSalary(Long id);
//
//    Double calculateNetSalary(Long id);
//
//    List<Salary> getAllSalaries();
//
//    SalarySlipDto getSalarySlipById(Long id);
//
//    // Dashboard Methods
//    long getTotalEmployees();
//
//    long getPaidEmployeesCount();
//
//    long getPendingEmployeesCount();
//
//    double getTotalPayrollCost();
//
//    double getTotalPaidSalary();
//
//    double getTotalPendingSalary();
//
//}















package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.SalarySlipDto;
import EmployeeManagementSystem.entity.Salary;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface SalaryService {

    Salary saveSalary(Salary salary);

    Salary getSalaryById(Long id);

    void deleteSalary(Long id);

    Double calculateNetSalary(Long id);

    List<Salary> getAllSalaries();

    SalarySlipDto getSalarySlipById(Long id);

    // Dashboard Methods
    long getTotalEmployees();

    long getPaidEmployeesCount();

    long getPendingEmployeesCount();

    double getTotalPayrollCost();

    double getTotalPaidSalary();

    double getTotalPendingSalary();

    List<Salary> findByEmployeeId(String employeeId);
}
