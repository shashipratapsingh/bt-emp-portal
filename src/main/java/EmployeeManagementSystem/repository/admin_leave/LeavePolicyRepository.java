package EmployeeManagementSystem.repository.admin_leave;

import EmployeeManagementSystem.entity.admin_leave.LeavePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeavePolicyRepository extends JpaRepository<LeavePolicy, Long> {
    List<LeavePolicy> findByLeaveTypeId(Long typeId);
}