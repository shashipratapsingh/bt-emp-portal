package EmployeeManagementSystem.repository;

import EmployeeManagementSystem.entity.LoginLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoginLocationRepository extends JpaRepository<LoginLocation, Long> {

    List<LoginLocation> findByEmployeeIdOrderByLoginTimeDesc(String employeeId);

}