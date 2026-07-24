package EmployeeManagementSystem.repository;

import EmployeeManagementSystem.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Search employees by keyword
    @Query("SELECT e FROM Employee e LEFT JOIN e.department d WHERE " +
            "LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(COALESCE(d.departmentName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Employee> searchAll(@Param("keyword") String keyword, Pageable pageable);

    // Search by full name using CONCAT
    @Query("SELECT e FROM Employee e WHERE " +
            "LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Employee> searchByName(@Param("keyword") String keyword);

    Employee getEmployeeById(Long id);

    // Find employee by email
    Optional<Employee> findByEmail(String email);

    // Employees having DOB
    @Query("SELECT e FROM Employee e WHERE e.dateOfBirth IS NOT NULL")
    List<Employee> findAllWithDob();

    // Employees having Joining Date
    @Query("SELECT e FROM Employee e WHERE e.joiningDate IS NOT NULL")
    List<Employee> findAllWithJoiningDate();

    // Current Month Birthdays
    @Query("SELECT e FROM Employee e WHERE MONTH(e.dateOfBirth) = MONTH(CURRENT_DATE)")
    List<Employee> findUpcomingBirthdays();

    // Work From Home Employees
    List<Employee> findByWorkMode(String workMode);

    // Find employees by department ID - Using Spring Data JPA naming convention
    List<Employee> findByDepartmentId(Long departmentId);
}