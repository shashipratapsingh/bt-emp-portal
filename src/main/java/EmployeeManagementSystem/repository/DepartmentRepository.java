package EmployeeManagementSystem.repository;

//import com.ems.entity.Department;
import EmployeeManagementSystem.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByDepartmentName(String departmentName);
    List<Department> findByStatus(String status);
    boolean existsByDepartmentName(String departmentName);
}