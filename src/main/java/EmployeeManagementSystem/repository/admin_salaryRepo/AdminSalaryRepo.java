package EmployeeManagementSystem.repository.admin_salaryRepo;

import EmployeeManagementSystem.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminSalaryRepo extends JpaRepository<Salary,Long> {
    List<Salary> findByEmployeeId(Long employeeId);

    Optional<Salary> findByEmployeeIdAndMonthAndYear(Long employeeId,
                                                     String month,
                                                     Integer year);
}
