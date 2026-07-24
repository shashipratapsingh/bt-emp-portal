package EmployeeManagementSystem.repository;

import EmployeeManagementSystem.entity.EmployeeProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {

    // Find by department name (since department is a String column)
    List<EmployeeProfile> findByDepartment(String department);

    // Using @Query with department name
    @Query("SELECT ep FROM EmployeeProfile ep WHERE ep.department = :departmentName")
    List<EmployeeProfile> findEmployeeProfilesByDepartment(@Param("departmentName") String departmentName);

    Optional<EmployeeProfile> findByUserId(String userId);

    Optional<EmployeeProfile> findByEmail(String email);

    List<EmployeeProfile> findByStatus(String status);

    Page<EmployeeProfile> findByStatus(String status, Pageable pageable);

    // Get the last employee for ID generation
    Optional<EmployeeProfile> findFirstByOrderByIdDesc();

    // ===== FIXED: Using fullName instead of firstName and lastName =====
    @Query("SELECT e FROM EmployeeProfile e WHERE " +
            "LOWER(e.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.userId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<EmployeeProfile> searchEmployees(@Param("keyword") String keyword);

    // ===== FIXED: Search with pagination using fullName =====
    @Query("SELECT e FROM EmployeeProfile e WHERE " +
            "LOWER(e.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.userId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<EmployeeProfile> searchEmployees(@Param("keyword") String keyword, Pageable pageable);

    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);

    // ===== Search by department ID using native query =====
    @Query(value = "SELECT * FROM employee_profiles WHERE department = (SELECT department_name FROM departments WHERE id = :departmentId)", nativeQuery = true)
    List<EmployeeProfile> findEmployeeProfilesByDepartmentId(@Param("departmentId") Long departmentId);
}