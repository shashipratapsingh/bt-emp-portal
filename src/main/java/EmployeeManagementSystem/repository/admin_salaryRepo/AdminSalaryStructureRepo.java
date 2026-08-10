// AdminSalaryStructureRepo.java
package EmployeeManagementSystem.repository.admin_salaryRepo;

import EmployeeManagementSystem.entity.admin_salary.SalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminSalaryStructureRepo extends JpaRepository<SalaryStructure, Long> {

    // ===== Methods using employee_profile_id =====

    @Query("SELECT ss FROM SalaryStructure ss WHERE ss.employeeProfileId = :profileId")
    Optional<SalaryStructure> findByEmployeeProfileId(@Param("profileId") Long profileId);

    @Query("SELECT ss FROM SalaryStructure ss WHERE ss.employeeProfile.id = :profileId")
    Optional<SalaryStructure> findByEmployeeProfile(@Param("profileId") Long profileId);

    @Query("SELECT CASE WHEN COUNT(ss) > 0 THEN true ELSE false END FROM SalaryStructure ss WHERE ss.employeeProfileId = :profileId")
    boolean existsByEmployeeProfileId(@Param("profileId") Long profileId);

    // ===== Remove these methods - they don't exist in the entity =====
    // @Query("SELECT ss FROM SalaryStructure ss WHERE ss.employeeId = :employeeId")
    // Optional<SalaryStructure> findByEmployeeId(@Param("employeeId") Long employeeId);

    // @Query("SELECT CASE WHEN COUNT(ss) > 0 THEN true ELSE false END FROM SalaryStructure ss WHERE ss.employeeId = :employeeId")
    // boolean existsByEmployeeId(@Param("employeeId") Long employeeId);
}