package EmployeeManagementSystem.service.admin_salary.impl;

import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.EmployeeProfile;
import EmployeeManagementSystem.entity.Salary;
import EmployeeManagementSystem.entity.admin_salary.SalaryStructure;
import EmployeeManagementSystem.repository.EmployeeProfileRepository;
import EmployeeManagementSystem.repository.admin_salaryRepo.AdminSalaryRepo;
import EmployeeManagementSystem.service.admin_salary.MonthlySalaryService;
import EmployeeManagementSystem.service.admin_salary.SalaryStructureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MonthlySalaryServiceImpl implements MonthlySalaryService {

    private final AdminSalaryRepo adminSalaryRepo;
    private final SalaryStructureService salaryStructureService;
    private final EmployeeProfileRepository employeeProfileRepository;

    @Override
    public Salary generateMonthlySalary(Long profileId, String month, Integer year) {
        EmployeeProfile employeeProfile = employeeProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Employee Profile not found with id: " + profileId));

        SalaryStructure salaryStructure = salaryStructureService.getSalaryStructureByEmployeeProfileId(profileId);

        // Check if salary already exists for this month
        if (adminSalaryRepo.existsByEmployeeProfileIdAndMonthAndYear(profileId, month, year)) {
            throw new RuntimeException("Salary already generated for " + month + " " + year);
        }

        // Calculate salary components
        double basicSalary = salaryStructure.getBasicSalary().doubleValue();
        double hra = salaryStructure.getHra().doubleValue();
        double allowance = salaryStructure.getConveyance().doubleValue() +
                salaryStructure.getMedicalAllowance().doubleValue() +
                salaryStructure.getSpecialAllowance().doubleValue() +
                salaryStructure.getOtherAllowance().doubleValue();
        double bonus = 0.0; // Can be calculated based on performance
        double deductions = salaryStructure.getPf().doubleValue() +
                salaryStructure.getEsi().doubleValue() +
                salaryStructure.getProfessionalTax().doubleValue() +
                salaryStructure.getTds().doubleValue() +
                salaryStructure.getLoanDeduction().doubleValue();

        double grossSalary = basicSalary + hra + allowance + bonus;
        double netSalary = grossSalary - deductions;

        Salary salary = new Salary();

        // Set Employee (via Employee object)
        Employee employee = employeeProfile.getEmployee();
        if (employee != null) {
            salary.setEmployee(employee);
        }

        // Set Employee Profile
        salary.setEmployeeProfileId(profileId);
        salary.setEmployeeProfile(employeeProfile);

        // Set other fields
        salary.setEmployeeCode(employeeProfile.getUserId());
        salary.setEmployeeName(employeeProfile.getFullName());
        salary.setDepartment(employeeProfile.getDepartment());
        salary.setDesignation(employeeProfile.getDesignation());
        salary.setBasicSalary(basicSalary);
        salary.setHra(hra);
        salary.setAllowance(allowance);
        salary.setBonus(bonus);
        salary.setDeductions(deductions);
        salary.setGrossSalary(grossSalary);
        salary.setNetSalary(netSalary);
        salary.setPaymentStatus("PENDING");
        salary.setMonth(month);
        salary.setYear(year);
        salary.setPaymentDate(null);

        return adminSalaryRepo.save(salary);
    }

    @Override
    public List<Salary> generateMonthlySalaryForAllEmployees(String month, Integer year) {
        List<EmployeeProfile> activeProfiles = employeeProfileRepository.findAll().stream()
                .filter(p -> "ACTIVE".equals(p.getStatus()))
                .collect(Collectors.toList());

        return activeProfiles.stream()
                .map(profile -> {
                    try {
                        return generateMonthlySalary(profile.getId(), month, year);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(salary -> salary != null)
                .collect(Collectors.toList());
    }

    @Override
    public Salary getSalaryByEmployeeProfileAndMonth(Long profileId, String month, Integer year) {
        return adminSalaryRepo.findByEmployeeProfileIdAndMonthAndYear(profileId, month, year)
                .orElseThrow(() -> new RuntimeException("Salary not found for employee profile " + profileId +
                        " for " + month + " " + year));
    }

    @Override
    public List<Salary> getSalariesByMonth(String month, Integer year) {
        return adminSalaryRepo.findByMonthAndYear(month, year);
    }

    @Override
    public List<Salary> getSalariesByEmployeeProfile(Long profileId) {
        return adminSalaryRepo.findByEmployeeProfileIdOrderByYearDescMonthDesc(profileId);
    }

    @Override
    public Salary getSalary(Long id) {
        return adminSalaryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Salary not found with id: " + id));
    }
}