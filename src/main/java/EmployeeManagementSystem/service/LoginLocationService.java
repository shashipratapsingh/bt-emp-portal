package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.LoginLocation;

import java.util.List;

public interface LoginLocationService {

    void save(LoginLocation loginLocation);

    List<LoginLocation> getAllLoginLocations();

    List<LoginLocation> getByEmployeeId(String employeeId);

    void delete(Long id);
}