//package EmployeeManagementSystem.repository;
//
//import EmployeeManagementSystem.entity.Salary;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface SalaryRepository extends JpaRepository<Salary, Long> {
//
//    List<Salary> findByMonthAndYear(String month, Integer year);
//
//    List<Salary> findByPaymentStatus(String paymentStatus);
//
//    long countByPaymentStatus(String paymentStatus);
//}
















package EmployeeManagementSystem.repository;

import EmployeeManagementSystem.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalaryRepository extends JpaRepository<Salary, Long> {

    List<Salary> findByMonthAndYear(String month, Integer year);

    List<Salary> findByPaymentStatus(String paymentStatus);

    long countByPaymentStatus(String paymentStatus);

    List<Salary> findByEmployeeId(String employeeId);

    @Query("SELECT s FROM Salary s WHERE s.employee.id = :employeeId")
    List<Salary> findSalariesByEmployeeId(@Param("employeeId") Long employeeId);
}
