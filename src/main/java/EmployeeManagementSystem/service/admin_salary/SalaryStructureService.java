package EmployeeManagementSystem.service.admin_salary;

import EmployeeManagementSystem.dto.SalaryStructureDTO;
import EmployeeManagementSystem.entity.admin_salary.SalaryStructure;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SalaryStructureService {
    @Transactional
    SalaryStructure saveSalaryStructure(SalaryStructureDTO dto);

    @Transactional
    SalaryStructure updateSalaryStructure(SalaryStructureDTO dto);

    SalaryStructure getSalaryStructure(Long id);

    SalaryStructure getSalaryStructureByEmployeeId(Long employeeId);

    List<SalaryStructure> getAllSalaryStructures();

    @Transactional
    void deleteSalaryStructure(Long id);

    boolean existsByEmployeeId(Long employeeId);
}
