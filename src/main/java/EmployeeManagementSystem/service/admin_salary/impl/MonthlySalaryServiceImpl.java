package EmployeeManagementSystem.service.admin_salary.impl;

import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.Salary;
import EmployeeManagementSystem.entity.admin_salary.SalaryStructure;
import EmployeeManagementSystem.repository.EmployeeRepository;
import EmployeeManagementSystem.repository.admin_salaryRepo.AdminSalaryRepo;
import EmployeeManagementSystem.repository.admin_salaryRepo.AdminSalaryStructureRepo;
import EmployeeManagementSystem.service.admin_salary.MonthlySalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonthlySalaryServiceImpl implements MonthlySalaryService {

    private final AdminSalaryRepo salaryRepository;
    private final AdminSalaryStructureRepo salaryStructureRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public Salary generateMonthlySalary(Long employeeId, String month, Integer year) {
        Optional<Salary> existing = salaryRepository.findByEmployeeIdAndMonthAndYear(employeeId, month, year);
        if (existing.isPresent()) {
            return existing.get();
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        SalaryStructure structure = salaryStructureRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Salary structure not found"));

        Salary salary = new Salary();
        salary.setEmployee(employee);
        salary.setEmployeeId(employee.getId().toString());
        salary.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
        salary.setDepartment(employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : null);
        salary.setDesignation(employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : null);

        // ----- Earnings -----
        salary.setBasicSalary(structure.getBasicSalary().doubleValue());
        salary.setHra(structure.getHra().doubleValue());

        // Allowance = sum of conveyance + medical + special + other
        double allowance = structure.getConveyance().doubleValue()
                + structure.getMedicalAllowance().doubleValue()
                + structure.getSpecialAllowance().doubleValue()
                + structure.getOtherAllowance().doubleValue();
        salary.setAllowance(allowance);

        salary.setBonus(0.0); // Can be set separately if needed

        // ----- Deductions -----
        double deductions = structure.getPf().doubleValue()
                + structure.getProfessionalTax().doubleValue()
                + structure.getTds().doubleValue();
        salary.setDeductions(deductions);

        // ----- Gross & Net -----
        double gross = salary.getBasicSalary() + salary.getHra() + salary.getAllowance() + salary.getBonus();
        salary.setGrossSalary(gross);
        salary.setNetSalary(gross - salary.getDeductions());

        // ----- Metadata -----
        salary.setMonth(month);
        salary.setYear(year);
        salary.setPaymentStatus("Pending");

        return salaryRepository.save(salary);
    }

    @Override
    @Transactional
    public List<Salary> generateMonthlySalaryForAllEmployees(String month, Integer year) {
        List<Employee> employees = employeeRepository.findAll();
        List<Salary> generated = new ArrayList<>();
        for (Employee emp : employees) {
            try {
                generated.add(generateMonthlySalary(emp.getId(), month, year));
            } catch (Exception e) {
                System.err.println("Error generating salary for employee " + emp.getId() + ": " + e.getMessage());
            }
        }
        return generated;
    }

    @Override
    public Salary getSalaryByEmployeeAndMonth(Long employeeId, String month, Integer year) {
        return salaryRepository.findByEmployeeIdAndMonthAndYear(employeeId, month, year)
                .orElseThrow(() -> new RuntimeException("Salary not found for employee: " + employeeId));
    }

    @Override
    public List<Salary> getSalariesByMonth(String month, Integer year) {
        return salaryRepository.findAll().stream()
                .filter(s -> month.equals(s.getMonth()) && year.equals(s.getYear()))
                .toList();
    }

    @Override
    public List<Salary> getSalariesByEmployee(Long employeeId) {
        return salaryRepository.findByEmployeeId(employeeId);
    }

    // Additional helper method for the slip view
    @Override
    public Salary getSalaryById(Long id) {
        return salaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salary not found with id: " + id));
    }
}