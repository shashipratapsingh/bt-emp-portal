package EmployeeManagementSystem.repository;

import EmployeeManagementSystem.entity.ProjectOffLog;
import EmployeeManagementSystem.enums.ProjectOffStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectOffRepository extends JpaRepository<ProjectOffLog,Long> {

    List<ProjectOffLog> findByStatus(ProjectOffStatus status);
}
