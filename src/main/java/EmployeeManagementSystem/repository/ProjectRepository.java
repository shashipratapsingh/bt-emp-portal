package EmployeeManagementSystem.repository;

import EmployeeManagementSystem.entity.Project;
import EmployeeManagementSystem.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // ===== EXISTING METHODS =====
    List<Project> findByAssignedEmployeesIsNotEmpty();
    List<Project> findAllByOrderByStatusAsc();

    @Query("SELECT p FROM Project p JOIN p.assignedEmployees e WHERE e.id = :employeeId")
    List<Project> findProjectsByEmployeeId(@Param("employeeId") Long employeeId);

    // Overlap year (uses onboardingDate and endDate)
    @Query("SELECT p FROM Project p " +
            "WHERE p.status IN ('ACTIVE', 'IN_PROGRESS', 'COMPLETED') " +
            "AND p.onboardingDate <= :endOfYear " +
            "AND (p.endDate IS NULL OR p.endDate >= :startOfYear)")
    List<Project> findProjectsOverlappingYear(@Param("startOfYear") LocalDate startOfYear,
                                              @Param("endOfYear") LocalDate endOfYear);

    // Overlap month
    @Query("SELECT p FROM Project p " +
            "WHERE p.onboardingDate <= :end " +
            "AND (p.endDate IS NULL OR p.endDate >= :start)")
    List<Project> findProjectsOverlappingMonth(@Param("start") LocalDate start,
                                               @Param("end") LocalDate end);

    // Find by onboarding date range
    List<Project> findProjectsByOnboardingDateBetween(LocalDate start, LocalDate end);

    List<Project> findProjectsByOnboardingDateBetweenAndStatus(LocalDate start, LocalDate end, ProjectStatus status);
    // Count by onboarding date range
    long countByOnboardingDateBetween(LocalDate start, LocalDate end);

}