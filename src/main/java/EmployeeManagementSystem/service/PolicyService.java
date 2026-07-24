package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.Policy;

import java.util.List;

public interface PolicyService {
    List<Policy> getAllPolicy();
    Policy getPolicyById(Long id);
    //List<Policy> getPoliciesByCategory(String category);
}
//package EmployeeManagementSystem.service;
//
//import EmployeeManagementSystem.entity.Policy;
//
//import java.util.List;
//
//public interface PolicyService {
//    List<Policy> getAllPolicy();
//    Policy getPolicyById(Long id);
//    List<Policy> getPoliciesByCategory(String category);
//}
