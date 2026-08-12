package EmployeeManagementSystem.repository;

import EmployeeManagementSystem.entity.Employee;
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

    @Query("SELECT e FROM EmployeeProfile e WHERE MONTH(e.dob) = MONTH(CURRENT_DATE)")
    List<EmployeeProfile> findUpcomingBirthdays();

    @Query("SELECT DISTINCT e.department FROM EmployeeProfile e WHERE e.department IS NOT NULL AND e.department != ''")
    List<String> findDistinctDepartments();

    // For filtering with keyword (you may need to adjust the query)
    @Query("SELECT e FROM EmployeeProfile e WHERE " +
            "(:keyword IS NULL OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.userId) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:department IS NULL OR e.department = :department) AND " +
            "(:status IS NULL OR e.status = :status)")
    Page<EmployeeProfile> searchByKeywordAndFilters(@Param("keyword") String keyword,
                                                    @Param("department") String department,
                                                    @Param("status") String status,
                                                    Pageable pageable);

    @Query("SELECT e FROM EmployeeProfile e WHERE " +
            "(:department IS NULL OR e.department = :department) AND " +
            "(:status IS NULL OR e.status = :status)")
    Page<EmployeeProfile> findByFilters(@Param("department") String department,
                                        @Param("status") String status,
                                        Pageable pageable);

    @Query("SELECT DISTINCT ep FROM EmployeeProfile ep " +
            "LEFT JOIN FETCH ep.salaryStructure ss " +
            "LEFT JOIN FETCH ep.projects p " +
            "LEFT JOIN FETCH ep.employee e " +
            "LEFT JOIN FETCH e.department d " +
            "WHERE ep.id = :id")
    Optional<EmployeeProfile> findByIdWithAllDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT ep FROM EmployeeProfile ep " +
            "LEFT JOIN ep.salaryStructure ss " +
            "LEFT JOIN ep.projects p " +
            "LEFT JOIN ep.employee e " +
            "LEFT JOIN e.department d " +
            "WHERE (:keyword IS NULL OR LOWER(ep.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(ep.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(ep.userId) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:departmentId IS NULL OR e.department.id = :departmentId) " +
            "AND (:status IS NULL OR ep.status = :status)")
    Page<EmployeeProfile> searchEmployeeProfiles(@Param("keyword") String keyword,
                                                 @Param("departmentId") Long departmentId,
                                                 @Param("status") String status,
                                                 Pageable pageable);

    @Query("SELECT DISTINCT ep.department FROM EmployeeProfile ep WHERE ep.department IS NOT NULL AND ep.department != ''")
    List<String> findAllDepartments();



    @Query("""
    SELECT ep
    FROM EmployeeProfile ep
    WHERE ep.department = :department
    AND ep.designation IN :designations
    """)
    List<EmployeeProfile> findReportingManagers(
            @Param("department") String department,
            @Param("designations") List<String> designations
    );
}

















//    @Query("SELECT DISTINCT ep FROM EmployeeProfile ep " +
//            "LEFT JOIN FETCH ep.salaryStructure ss " +
//            "LEFT JOIN FETCH ep.projects p " +
//            "LEFT JOIN FETCH ep.employee e " +
//            "LEFT JOIN FETCH e.department d " +
//            "WHERE ep.id = :id")
//    Optional<EmployeeProfile> findByIdWithAllDetails(@Param("id") Long id);
//
//    @Query("SELECT DISTINCT ep FROM EmployeeProfile ep " +
//            "LEFT JOIN ep.salaryStructure ss " +
//            "LEFT JOIN ep.projects p " +
//            "LEFT JOIN ep.employee e " +
//            "LEFT JOIN e.department d " +
//            "WHERE (:keyword IS NULL OR LOWER(ep.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "OR LOWER(ep.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "OR LOWER(ep.userId) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
//            "AND (:departmentId IS NULL OR e.department.id = :departmentId) " +
//            "AND (:status IS NULL OR ep.status = :status)")
//    Page<EmployeeProfile> searchEmployeeProfiles(@Param("keyword") String keyword,
//                                                 @Param("departmentId") Long departmentId,
//                                                 @Param("status") String status,
//                                                 Pageable pageable);