package EmployeeManagementSystem.repository;

import EmployeeManagementSystem.entity.WfhRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WfhRequestRepository extends JpaRepository<WfhRequest,Long> {

    List<WfhRequest> findByStatus(String status);
    long countByStatus(String status);
}
