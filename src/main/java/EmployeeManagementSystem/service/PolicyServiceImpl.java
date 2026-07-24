package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.Policy;
import EmployeeManagementSystem.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService{
    private final PolicyRepository policyRepository;
    @Override
    public List<Policy> getAllPolicy(){
        return policyRepository.findAll();
    }
    @Override
    public Policy getPolicyById(Long id){
        return policyRepository.findById(id).orElse(null);
    }
//    @Override
//    public List<Policy> grtPoliciesByCategory(String category){
//        return policyRepository.
//    }
}
//package EmployeeManagementSystem.service;
//
//import EmployeeManagementSystem.entity.Policy;
//import EmployeeManagementSystem.repository.PolicyRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class PolicyServiceImpl implements PolicyService{
//    private final PolicyRepository policyRepository;
//    @Override
//    public List<Policy> getAllPolicy(){
//        return policyRepository.findAll();
//    }
//    @Override
//    public Policy getPolicyById(Long id){
//        return policyRepository.findById(id).orElse(null);
//    }
//    @Override
//    public List<Policy> grtPoliciesByCategory(String category){
//        return policyRepository.
//    }
//}
