package EmployeeManagementSystem.repository;

import EmployeeManagementSystem.entity.UserSession;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession,Long> {
    Optional<UserSession> findByJwtTokenAndIsActiveTrue(String jwtToken);

    //List<UserSession> findByEmployeeIdAndIsActiveTrue(String employeeId);
    @Modifying
    @Transactional
    @Query("update UserSession u set u.isActive = false")
    void deactivateAllSessions();
}
