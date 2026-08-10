// SalaryStructureService.java
package EmployeeManagementSystem.service.admin_salary;

import EmployeeManagementSystem.dto.SalaryStructureDTO;
import EmployeeManagementSystem.entity.admin_salary.SalaryStructure;

import java.util.List;

public interface SalaryStructureService {

    SalaryStructure saveSalaryStructure(SalaryStructureDTO dto);

    SalaryStructure updateSalaryStructure(SalaryStructureDTO dto);

    SalaryStructure getSalaryStructure(Long id);

    SalaryStructure getSalaryStructureByEmployeeProfileId(Long profileId);

    List<SalaryStructure> getAllSalaryStructures();

    void deleteSalaryStructure(Long id);

    boolean existsByEmployeeProfileId(Long profileId);
}