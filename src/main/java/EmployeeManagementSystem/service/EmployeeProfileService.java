package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.CelebrationDto;
import EmployeeManagementSystem.dto.EmployeeCredentialsDTO;
import EmployeeManagementSystem.entity.EmployeeProfile;
import EmployeeManagementSystem.repository.EmployeeProfileRepository;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;        // ✅ CORRECT
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface EmployeeProfileService {
    EmployeeProfile saveEmployeeProfile(EmployeeProfile employeeProfile);

    EmployeeProfile updateEmployeeProfile(Long id, EmployeeProfile employeeProfile);

    void deleteEmployeeProfile(Long id);

    EmployeeProfile getEmployeeProfileById(Long id);

    @Nullable
    Object getAllEmployeeProfiles();

    // Query by department name (String)
    List<EmployeeProfile> getEmployeeProfilesByDepartment(String departmentName);

    // Query by department ID - converts ID to department name then queries
    List<EmployeeProfile> getEmployeeProfilesByDepartment(Long departmentId);

    EmployeeProfile getEmployeeProfileByUserId(String userId);

    EmployeeProfile getEmployeeProfileByEmail(String email);

    List<EmployeeProfile> getEmployeeProfilesByStatus(String status);

    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);

    long countEmployeeProfiles();

    List<EmployeeProfile> searchEmployees(String keyword);

    String generateUserId();

    String generateDefaultPassword();

    EmployeeProfile registerEmployee(EmployeeProfile employeeProfile);

    void updateEmployeePassword(Long id, String newPassword);

    EmployeeCredentialsDTO saveOrUpdateProfile(@Valid EmployeeProfile employee, Object o);

    String uploadPhoto(MultipartFile photoFile, String userId);

    Page<EmployeeProfile> getAllProfiles(Pageable pageable);

    Optional<Object> getProfileById(Long id);

    String updateEmployee(Long id, @Valid EmployeeProfile employee, MultipartFile photoFile);

    void deleteEmployee(Long id);

    void softDeleteEmployee(Long id);

    EmployeeProfile getProfileByUserId(String currentEmplId);

    public List<CelebrationDto> getTodayCelebrations();
}
