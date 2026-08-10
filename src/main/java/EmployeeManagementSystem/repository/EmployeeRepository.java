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

    // Find employee by email
    Optional<Employee> findByEmail(String email);

    // Find employees by department ID - Using Spring Data JPA naming convention
    List<Employee> findByDepartmentId(Long departmentId);

    @Query("SELECT DISTINCT e FROM Employee e " +
            "LEFT JOIN FETCH e.profile p " +
            "LEFT JOIN FETCH e.department d " +
            "LEFT JOIN FETCH e.salaries s " +
            "WHERE e.id = :id")
    Optional<Employee> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT e FROM Employee e " +
            "LEFT JOIN e.profile p " +
            "LEFT JOIN e.department d " +
            "WHERE (:keyword IS NULL OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.userId) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:departmentId IS NULL OR e.department.id = :departmentId) " +
            "AND (:status IS NULL OR p.status = :status)")
    Page<Employee> searchEmployees(@Param("keyword") String keyword,
                                   @Param("departmentId") Long departmentId,
                                   @Param("status") String status,
                                   Pageable pageable);



    @Query("SELECT DISTINCT e FROM Employee e " +
            "LEFT JOIN e.profile p " +
            "WHERE p.status = 'ACTIVE'")
    List<Employee> findAllActiveEmployees();


}