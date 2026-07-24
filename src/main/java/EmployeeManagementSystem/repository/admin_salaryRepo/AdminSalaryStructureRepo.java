package EmployeeManagementSystem.repository.admin_salaryRepo;

import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.admin_salary.SalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminSalaryStructureRepo extends JpaRepository<SalaryStructure, Long> {
    Optional<SalaryStructure> findByEmployee(Employee employee);

    Optional<SalaryStructure> findByEmployeeId(Long employeeId);

    boolean existsByEmployeeId(Long employeeId);

    @Query("SELECT ss FROM SalaryStructure ss JOIN FETCH ss.employee e JOIN FETCH e.department")
    List<SalaryStructure> findAllWithEmployeeAndDepartment();
}
