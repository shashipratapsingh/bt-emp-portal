package EmployeeManagementSystem.repository;

import EmployeeManagementSystem.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByAssignedEmployeesIsNotEmpty();
    List<Project> findAllByOrderByStatusAsc();


    @Query("SELECT p FROM Project p JOIN p.assignedEmployees e WHERE e.id = :employeeId")
    List<Project> findProjectsByEmployeeId(@Param("employeeId") Long employeeId);
}
