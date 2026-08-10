package EmployeeManagementSystem.repository.admin_salaryRepo;

import EmployeeManagementSystem.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminSalaryRepo extends JpaRepository<Salary, Long> {

    // Methods using EmployeeProfile
    @Query("SELECT s FROM Salary s WHERE s.employeeProfileId = :profileId AND s.month = :month AND s.year = :year")
    Optional<Salary> findByEmployeeProfileIdAndMonthAndYear(@Param("profileId") Long profileId,
                                                            @Param("month") String month,
                                                            @Param("year") Integer year);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Salary s WHERE s.employeeProfileId = :profileId AND s.month = :month AND s.year = :year")
    boolean existsByEmployeeProfileIdAndMonthAndYear(@Param("profileId") Long profileId,
                                                     @Param("month") String month,
                                                     @Param("year") Integer year);

    @Query("SELECT s FROM Salary s WHERE s.employeeProfileId = :profileId ORDER BY s.year DESC, s.month DESC")
    List<Salary> findByEmployeeProfileIdOrderByYearDescMonthDesc(@Param("profileId") Long profileId);

    @Query("SELECT s FROM Salary s WHERE s.month = :month AND s.year = :year ORDER BY s.employeeName ASC")
    List<Salary> findByMonthAndYear(@Param("month") String month, @Param("year") Integer year);
}