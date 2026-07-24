package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.Department;
import EmployeeManagementSystem.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department saveDepartment(Department department) {
        // Set default designations if not provided
        if (department.getDesignations() == null || department.getDesignations().isEmpty()) {
            department.setDesignations(getDefaultDesignations(department.getDepartmentName()));
        }

        if (department.getCreatedAt() == null) {
            department.setCreatedAt(LocalDateTime.now());
        }
        department.setUpdatedAt(LocalDateTime.now());

        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(Long id, Department department) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        existing.setDepartmentName(department.getDepartmentName());
        existing.setDescription(department.getDescription());
        existing.setStatus(department.getStatus());
        existing.setDesignations(department.getDesignations());
        existing.setUpdatedAt(LocalDateTime.now());

        return departmentRepository.save(existing);
    }

    @Override
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public List<Department> getActiveDepartments() {
        return departmentRepository.findByStatus("ACTIVE");
    }

    @Override
    public Optional<Department> getDepartmentByName(String name) {
        return departmentRepository.findByDepartmentName(name);
    }

    @Override
    public List<String> getDesignationsByDepartment(String departmentName) {
        Optional<Department> dept = departmentRepository.findByDepartmentName(departmentName);
        if (dept.isPresent() && dept.get().getDesignations() != null &&
                !dept.get().getDesignations().isEmpty()) {
            return dept.get().getDesignations();
        }
        return getDefaultDesignations(departmentName);
    }

    @Override
    public boolean existsByDepartmentName(String name) {
        return departmentRepository.existsByDepartmentName(name);
    }

    @Override
    public Department findByDepartmentName(String department) {
        return departmentRepository.findByDepartmentName(department).orElse(null);
    }


    private List<String> getDefaultDesignations(String departmentName) {
        Map<String, List<String>> defaultDesignations = new HashMap<>();

        defaultDesignations.put("IT", Arrays.asList(
                "Software Developer", "Senior Developer", "Tech Lead"
        ));

        defaultDesignations.put("Human Resources", Arrays.asList(
                "HR Manager", "HR Executive", "Recruiter"
        ));

        defaultDesignations.put("Sales", Arrays.asList(
                "Sales Manager", "Sales Executive", "Business Development"
        ));

        defaultDesignations.put("Marketing", Arrays.asList(
                "Marketing Manager", "Digital Marketing", "Content Writer"
        ));

        defaultDesignations.put("Finance", Arrays.asList(
                "Finance Manager", "Accountant", "Financial Analyst"
        ));

        return defaultDesignations.getOrDefault(departmentName,
                Arrays.asList("Manager", "Executive", "Associate"));
    }
}