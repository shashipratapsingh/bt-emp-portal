package EmployeeManagementSystem.repository;

import EmployeeManagementSystem.dto.LoginRequest;
import EmployeeManagementSystem.entity.RegisterEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisterEmployeeRepository extends JpaRepository<RegisterEmployee,Integer> {
    RegisterEmployee findByUserId(String userId);
    Optional<RegisterEmployee> findFirstByOrderByIdDesc();
    Optional<RegisterEmployee> findByEmail(String email);
    @Query(value = "SELECT * FROM register_employee WHERE " +
            "DATE_FORMAT(dob, '%m-%d') BETWEEN DATE_FORMAT(CURDATE(), '%m-%d') " +
            "AND DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 7 DAY), '%m-%d') " +
            "ORDER BY DATE_FORMAT(dob, '%m-%d') ASC", nativeQuery = true)
    List<RegisterEmployee> findUpcomingBirthdays();
}
