package EmployeeManagementSystem.service.admin_salary.impl;



import EmployeeManagementSystem.dto.SalaryStructureDTO;
import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.admin_salary.SalaryStructure;
import EmployeeManagementSystem.repository.EmployeeRepository;
import EmployeeManagementSystem.repository.admin_salaryRepo.AdminSalaryStructureRepo;
import EmployeeManagementSystem.service.admin_salary.SalaryStructureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryStructureServiceImpl implements SalaryStructureService {

    private final AdminSalaryStructureRepo salaryStructureRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    @Override
    public SalaryStructure saveSalaryStructure(SalaryStructureDTO dto) {
        // Find employee
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + dto.getEmployeeId()));

        // Check if salary structure already exists
        if (salaryStructureRepository.existsByEmployeeId(dto.getEmployeeId())) {
            throw new RuntimeException("Salary structure already exists for employee: " + dto.getEmployeeId());
        }

        // Create Salary Structure
        SalaryStructure salaryStructure = SalaryStructure.builder()
                .employee(employee)
                .basicSalary(dto.getBasicSalary() != null ? dto.getBasicSalary() : BigDecimal.ZERO)
                .hra(dto.getHra() != null ? dto.getHra() : BigDecimal.ZERO)
                .conveyance(dto.getConveyance() != null ? dto.getConveyance() : BigDecimal.ZERO)
                .medicalAllowance(dto.getMedicalAllowance() != null ? dto.getMedicalAllowance() : BigDecimal.ZERO)
                .specialAllowance(dto.getSpecialAllowance() != null ? dto.getSpecialAllowance() : BigDecimal.ZERO)
                .otherAllowance(dto.getOtherAllowance() != null ? dto.getOtherAllowance() : BigDecimal.ZERO)
                .pf(dto.getPf() != null ? dto.getPf() : BigDecimal.ZERO)
                .esi(dto.getEsi() != null ? dto.getEsi() : BigDecimal.ZERO)
                .professionalTax(dto.getProfessionalTax() != null ? dto.getProfessionalTax() : BigDecimal.ZERO)
                .tds(dto.getTds() != null ? dto.getTds() : BigDecimal.ZERO)
                .loanDeduction(dto.getLoanDeduction() != null ? dto.getLoanDeduction() : BigDecimal.ZERO)
                .effectiveFrom(dto.getEffectiveFrom() != null ? dto.getEffectiveFrom() : LocalDate.now())
                .build();

        return salaryStructureRepository.save(salaryStructure);
    }

    @Transactional
    @Override
    public SalaryStructure updateSalaryStructure(SalaryStructureDTO dto) {
        // Find existing salary structure
        SalaryStructure existing = salaryStructureRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Salary structure not found with id: " + dto.getEmployeeId()));

        // Update fields
        existing.setBasicSalary(dto.getBasicSalary() != null ? dto.getBasicSalary() : BigDecimal.ZERO);
        existing.setHra(dto.getHra() != null ? dto.getHra() : BigDecimal.ZERO);
        existing.setConveyance(dto.getConveyance() != null ? dto.getConveyance() : BigDecimal.ZERO);
        existing.setMedicalAllowance(dto.getMedicalAllowance() != null ? dto.getMedicalAllowance() : BigDecimal.ZERO);
        existing.setSpecialAllowance(dto.getSpecialAllowance() != null ? dto.getSpecialAllowance() : BigDecimal.ZERO);
        existing.setOtherAllowance(dto.getOtherAllowance() != null ? dto.getOtherAllowance() : BigDecimal.ZERO);
        existing.setPf(dto.getPf() != null ? dto.getPf() : BigDecimal.ZERO);
        existing.setEsi(dto.getEsi() != null ? dto.getEsi() : BigDecimal.ZERO);
        existing.setProfessionalTax(dto.getProfessionalTax() != null ? dto.getProfessionalTax() : BigDecimal.ZERO);
        existing.setTds(dto.getTds() != null ? dto.getTds() : BigDecimal.ZERO);
        existing.setLoanDeduction(dto.getLoanDeduction() != null ? dto.getLoanDeduction() : BigDecimal.ZERO);

        if (dto.getEffectiveFrom() != null) {
            existing.setEffectiveFrom(dto.getEffectiveFrom());
        }

        return salaryStructureRepository.save(existing);
    }

    @Override
    public SalaryStructure getSalaryStructure(Long id) {
        return salaryStructureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salary structure not found with id: " + id));
    }

    @Override
    public SalaryStructure getSalaryStructureByEmployeeId(Long employeeId) {
        return salaryStructureRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Salary structure not found for employee id: " + employeeId));
    }

    public List<SalaryStructure> getAllSalaryStructures() {
        return salaryStructureRepository.findAllWithEmployeeAndDepartment();
    }

    @Transactional
    @Override
    public void deleteSalaryStructure(Long id) {
        SalaryStructure salaryStructure = getSalaryStructure(id);
        salaryStructureRepository.delete(salaryStructure);
    }

    @Override
    public boolean existsByEmployeeId(Long employeeId) {
        return salaryStructureRepository.existsByEmployeeId(employeeId);
    }
}
