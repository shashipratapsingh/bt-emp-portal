package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.LoginLocation;
import EmployeeManagementSystem.repository.LoginLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginLocationServiceImpl implements LoginLocationService {

    private final LoginLocationRepository repository;


//     Save employee login location
    @Override
    public void save(LoginLocation loginLocation) {
        repository.save(loginLocation);
    }


//     Get all login locations
    @Override
    public List<LoginLocation> getAllLoginLocations() {
        return repository.findAll();
    }


//      Get login history by employee ID
    @Override
    public List<LoginLocation> getByEmployeeId(String employeeId) {
        return repository.findByEmployeeIdOrderByLoginTimeDesc(employeeId);
    }


//      Delete login location
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}