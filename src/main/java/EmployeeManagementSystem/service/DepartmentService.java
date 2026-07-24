package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.Department;
import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Department saveDepartment(Department department);
    Department updateDepartment(Long id, Department department);
    void deleteDepartment(Long id);
    Department getDepartmentById(Long id);
    List<Department> getAllDepartments();
    List<Department> getActiveDepartments();
    Optional<Department> getDepartmentByName(String name);
    List<String> getDesignationsByDepartment(String departmentName);
    boolean existsByDepartmentName(String name);

    Department findByDepartmentName(String department);
}