// SalaryStructureServiceImpl.java
package EmployeeManagementSystem.service.admin_salary.impl;

import EmployeeManagementSystem.dto.SalaryStructureDTO;
import EmployeeManagementSystem.entity.EmployeeProfile;
import EmployeeManagementSystem.entity.admin_salary.SalaryStructure;
import EmployeeManagementSystem.repository.EmployeeProfileRepository;
import EmployeeManagementSystem.repository.admin_salaryRepo.AdminSalaryStructureRepo;
import EmployeeManagementSystem.service.admin_salary.SalaryStructureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalaryStructureServiceImpl implements SalaryStructureService {

    private final AdminSalaryStructureRepo salaryStructureRepo;
    private final EmployeeProfileRepository employeeProfileRepository;

    @Override
    public SalaryStructure saveSalaryStructure(SalaryStructureDTO dto) {
        EmployeeProfile employeeProfile = employeeProfileRepository.findById(dto.getEmployeeProfileId())
                .orElseThrow(() -> new RuntimeException("Employee Profile not found with id: " + dto.getEmployeeProfileId()));

        SalaryStructure salaryStructure = new SalaryStructure();
        salaryStructure.setEmployeeProfileId(employeeProfile.getId());
        salaryStructure.setEmployeeProfile(employeeProfile);
        salaryStructure.setBasicSalary(dto.getBasicSalary());
        salaryStructure.setHra(dto.getHra());
        salaryStructure.setConveyance(dto.getConveyance());
        salaryStructure.setMedicalAllowance(dto.getMedicalAllowance());
        salaryStructure.setSpecialAllowance(dto.getSpecialAllowance());
        salaryStructure.setOtherAllowance(dto.getOtherAllowance());
        salaryStructure.setPf(dto.getPf());
        salaryStructure.setEsi(dto.getEsi());
        salaryStructure.setProfessionalTax(dto.getProfessionalTax());
        salaryStructure.setTds(dto.getTds());
        salaryStructure.setLoanDeduction(dto.getLoanDeduction());
        salaryStructure.setEffectiveFrom(dto.getEffectiveFrom());

        return salaryStructureRepo.save(salaryStructure);
    }

    @Override
    public SalaryStructure updateSalaryStructure(SalaryStructureDTO dto) {
        SalaryStructure existing = salaryStructureRepo.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Salary structure not found with id: " + dto.getId()));

        existing.setBasicSalary(dto.getBasicSalary());
        existing.setHra(dto.getHra());
        existing.setConveyance(dto.getConveyance());
        existing.setMedicalAllowance(dto.getMedicalAllowance());
        existing.setSpecialAllowance(dto.getSpecialAllowance());
        existing.setOtherAllowance(dto.getOtherAllowance());
        existing.setPf(dto.getPf());
        existing.setEsi(dto.getEsi());
        existing.setProfessionalTax(dto.getProfessionalTax());
        existing.setTds(dto.getTds());
        existing.setLoanDeduction(dto.getLoanDeduction());
        existing.setEffectiveFrom(dto.getEffectiveFrom());

        return salaryStructureRepo.save(existing);
    }

    @Override
    public SalaryStructure getSalaryStructure(Long id) {
        return salaryStructureRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Salary structure not found with id: " + id));
    }

    @Override
    public SalaryStructure getSalaryStructureByEmployeeProfileId(Long profileId) {
        return salaryStructureRepo.findByEmployeeProfileId(profileId)
                .orElseThrow(() -> new RuntimeException("Salary structure not found for profile id: " + profileId));
    }

    @Override
    public List<SalaryStructure> getAllSalaryStructures() {
        return salaryStructureRepo.findAll();
    }

    @Override
    public void deleteSalaryStructure(Long id) {
        salaryStructureRepo.deleteById(id);
    }

    @Override
    public boolean existsByEmployeeProfileId(Long profileId) {
        return salaryStructureRepo.existsByEmployeeProfileId(profileId);
    }
}