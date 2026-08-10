
package EmployeeManagementSystem.repository;

import EmployeeManagementSystem.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary, Long> {

    List<Salary> findByEmployeeId(String employeeId);

    @Query("SELECT s FROM Salary s WHERE s.employee.id = :employeeId")
    List<Salary> findSalariesByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT s FROM Salary s WHERE s.employee.id = :employeeId ORDER BY s.year DESC, " +
            "CASE s.month " +
            "WHEN 'January' THEN 1 WHEN 'February' THEN 2 WHEN 'March' THEN 3 " +
            "WHEN 'April' THEN 4 WHEN 'May' THEN 5 WHEN 'June' THEN 6 " +
            "WHEN 'July' THEN 7 WHEN 'August' THEN 8 WHEN 'September' THEN 9 " +
            "WHEN 'October' THEN 10 WHEN 'November' THEN 11 WHEN 'December' THEN 12 END DESC")
    List<Salary> findByEmployeeIdOrderByYearMonthDesc(@Param("employeeId") Long employeeId);

    @Query("SELECT s FROM Salary s WHERE s.employee.id = :employeeId AND s.paymentStatus = 'PAID'")
    List<Salary> findPaidSalariesByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT s FROM Salary s WHERE s.employee.id = :employeeId AND s.month = :month AND s.year = :year")
    Optional<Salary> findByEmployeeIdAndMonthYear(@Param("employeeId") Long employeeId,
                                                  @Param("month") String month,
                                                  @Param("year") Integer year);

    @Query("SELECT s FROM Salary s WHERE s.paymentStatus = 'UNPAID' OR s.paymentStatus IS NULL")
    List<Salary> findAllUnpaidSalaries();

}
