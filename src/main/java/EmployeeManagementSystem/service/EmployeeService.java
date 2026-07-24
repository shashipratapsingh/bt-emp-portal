package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.AnniversaryDTO;
import EmployeeManagementSystem.dto.BirthdayDTO;
import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.EmployeeProfile;
import EmployeeManagementSystem.entity.RegisterEmployee;
import EmployeeManagementSystem.repository.EmployeeRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    Employee updateEmployee(Long id, Employee employee);

    void deleteEmployee(Long id);

   Employee getEmployeeById(Long id);



    Page<Employee> getAllEmployees(int pageNo, int pageSize, String sortBy);

    Page<Employee> searchEmployee(String keyword, Pageable pageable);

    List<Employee> getAllEmployeesList();

    Employee findByEmail(String email);

    long totalEmployees();

    List<BirthdayDTO> getUpcomingBirthdays();

    List<AnniversaryDTO> getUpcomingAnniversaries();

    List<Employee> getAllEmployees();

    List<Employee> getEmployeesByIds(List<Long> ids);

    // Fix: Change return type from Collection<Object> to List<Employee>
    List<Employee> getEmployeesByDepartment(Long departmentId);


    @Nullable Object findAll();
}