package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.CelebrationDto;
import EmployeeManagementSystem.dto.EmployeeCredentialsDTO;
import EmployeeManagementSystem.entity.Department;
import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.EmployeeProfile;
import EmployeeManagementSystem.repository.EmployeeProfileRepository;
import EmployeeManagementSystem.service.DepartmentService;
import EmployeeManagementSystem.service.EmployeeProfileService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    @Autowired
    private EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    private DepartmentService departmentService;

    // ===== Basic CRUD Operations =====

    @Override
    public EmployeeProfile saveEmployeeProfile(EmployeeProfile employeeProfile) {
        if (employeeProfile.getUserId() == null || employeeProfile.getUserId().isEmpty()) {
            employeeProfile.setUserId(generateUserId());
        }

        if (employeeProfile.getPassword() == null || employeeProfile.getPassword().isEmpty()) {
            employeeProfile.setPassword(generateDefaultPassword());
        }

        if (employeeProfile.getCreatedAt() == null) {
            employeeProfile.setCreatedAt(LocalDateTime.now());
        }
        if (employeeProfile.getRegisteredAt() == null) {
            employeeProfile.setRegisteredAt(LocalDateTime.now());
        }
        employeeProfile.setUpdatedAt(LocalDateTime.now());

        if (employeeProfile.getStatus() == null) {
            employeeProfile.setStatus("ACTIVE");
        }

        return employeeProfileRepository.save(employeeProfile);
    }

    @Override
    public EmployeeProfile updateEmployeeProfile(Long id, EmployeeProfile employeeProfile) {
        EmployeeProfile existing = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EmployeeProfile not found with id: " + id));

        existing.setFullName(employeeProfile.getFullName());
        existing.setPhoneNumber(employeeProfile.getPhoneNumber());
        existing.setDob(employeeProfile.getDob());
        existing.setGender(employeeProfile.getGender());
        existing.setBloodGroup(employeeProfile.getBloodGroup());
        existing.setEmail(employeeProfile.getEmail());
        existing.setMaritalStatus(employeeProfile.getMaritalStatus());
        existing.setFieldOfStudy(employeeProfile.getFieldOfStudy());
        existing.setHighestQualification(employeeProfile.getHighestQualification());
        existing.setUniversity(employeeProfile.getUniversity());
        existing.setPassingYear(employeeProfile.getPassingYear());
        existing.setBankName(employeeProfile.getBankName());
        existing.setAccountNumber(employeeProfile.getAccountNumber());
        existing.setAccountHolderName(employeeProfile.getAccountHolderName());
        existing.setIfscCode(employeeProfile.getIfscCode());
        existing.setCurrentAddress(employeeProfile.getCurrentAddress());
        existing.setPermanentAddress(employeeProfile.getPermanentAddress());
        existing.setPhoto(employeeProfile.getPhoto());
        existing.setDepartment(employeeProfile.getDepartment());
        existing.setDesignation(employeeProfile.getDesignation());
        existing.setStatus(employeeProfile.getStatus());
        existing.setEmailSent(employeeProfile.isEmailSent());
        existing.setUpdatedAt(LocalDateTime.now());

        return employeeProfileRepository.save(existing);
    }

    @Override
    public void deleteEmployeeProfile(Long id) {
        EmployeeProfile existing = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EmployeeProfile not found with id: " + id));
        employeeProfileRepository.delete(existing);
    }

    @Override
    public EmployeeProfile getEmployeeProfileById(Long id) {
        return employeeProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EmployeeProfile not found with id: " + id));
    }

    @Override
    @Nullable
    public Object getAllEmployeeProfiles() {
        List<EmployeeProfile> profiles = employeeProfileRepository.findAll();
        if (profiles.isEmpty()) {
            return null;
        }
        return profiles;
    }

    // ===== Query Methods =====

    @Override
    public List<EmployeeProfile> getEmployeeProfilesByDepartment(String departmentName) {
        if (departmentName == null || departmentName.isEmpty()) {
            return new ArrayList<>();
        }
        return employeeProfileRepository.findByDepartment(departmentName);
    }

    @Override
    public List<EmployeeProfile> getEmployeeProfilesByDepartment(Long departmentId) {
        if (departmentId == null) {
            return new ArrayList<>();
        }

        try {
            Department department = departmentService.getDepartmentById(departmentId);
            String departmentName = department.getDepartmentName();
            return employeeProfileRepository.findByDepartment(departmentName);
        } catch (Exception e) {
            System.err.println("Error fetching employee profiles by department ID: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public EmployeeProfile getEmployeeProfileByUserId(String userId) {
        return employeeProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("EmployeeProfile not found with userId: " + userId));
    }

    @Override
    public EmployeeProfile getEmployeeProfileByEmail(String email) {
        return employeeProfileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("EmployeeProfile not found with email: " + email));
    }

    @Override
    public List<EmployeeProfile> getEmployeeProfilesByStatus(String status) {
        return employeeProfileRepository.findByStatus(status);
    }

    @Override
    public boolean existsByUserId(String userId) {
        return employeeProfileRepository.existsByUserId(userId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return employeeProfileRepository.existsByEmail(email);
    }

    @Override
    public long countEmployeeProfiles() {
        return employeeProfileRepository.count();
    }

    @Override
    public List<EmployeeProfile> searchEmployees(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return employeeProfileRepository.findAll();
        }
        return employeeProfileRepository.searchEmployees(keyword);
    }

    // ===== Utility Methods =====

    @Override
    public String generateUserId() {
        EmployeeProfile lastProfile = employeeProfileRepository.findFirstByOrderByIdDesc().orElse(null);

        if (lastProfile == null) {
            return "EMP0001";
        }

        String lastUserId = lastProfile.getUserId();
        if (lastUserId == null || lastUserId.isEmpty()) {
            return "EMP0001";
        }

        String numericPart = lastUserId.replaceAll("[^0-9]", "");
        if (numericPart.isEmpty()) {
            return "EMP0001";
        }

        try {
            int lastNumber = Integer.parseInt(numericPart);
            int nextNumber = lastNumber + 1;
            return String.format("EMP%04d", nextNumber);
        } catch (NumberFormatException e) {
            return "EMP0001";
        }
    }

    @Override
    public String generateDefaultPassword() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public EmployeeProfile registerEmployee(EmployeeProfile employeeProfile) {
        employeeProfile.setUserId(generateUserId());
        employeeProfile.setPassword(generateDefaultPassword());
        employeeProfile.setRegisteredAt(LocalDateTime.now());
        employeeProfile.setCreatedAt(LocalDateTime.now());
        employeeProfile.setUpdatedAt(LocalDateTime.now());

        if (employeeProfile.getStatus() == null) {
            employeeProfile.setStatus("ACTIVE");
        }

        employeeProfile.setEmailSent(false);

        return employeeProfileRepository.save(employeeProfile);
    }

    @Override
    public void updateEmployeePassword(Long id, String newPassword) {
        EmployeeProfile existing = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EmployeeProfile not found with id: " + id));
        existing.setPassword(newPassword);
        existing.setUpdatedAt(LocalDateTime.now());
        employeeProfileRepository.save(existing);
    }

    // ===== New Methods Implementation =====

    @Override
    public EmployeeCredentialsDTO saveOrUpdateProfile(@Valid EmployeeProfile employee, Object o) {
        // Save the employee profile
        EmployeeProfile savedProfile = saveEmployeeProfile(employee);

        // Create and return credentials DTO
        EmployeeCredentialsDTO credentials = new EmployeeCredentialsDTO();
        credentials.setUserId(savedProfile.getUserId());
        credentials.setPassword(savedProfile.getPassword());
        credentials.setEmail(savedProfile.getEmail());
        credentials.setFullName(savedProfile.getFullName());

        return credentials;
    }

    @Override
    public String uploadPhoto(MultipartFile photoFile, String userId) {
        try {
            // Get employee by userId
            EmployeeProfile employee = getEmployeeProfileByUserId(userId);

            // Define upload directory
            String uploadDir = "C:/New folder/uploadFile/";
            Path uploadPath = Paths.get(uploadDir);

            // Create directory if it doesn't exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = photoFile.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = userId + "_" + System.currentTimeMillis() + fileExtension;
            Path filePath = uploadPath.resolve(fileName);

            // Save file
            Files.write(filePath, photoFile.getBytes());

            // Update employee profile with photo path
            employee.setPhoto(fileName);
            employee.setUpdatedAt(LocalDateTime.now());
            employeeProfileRepository.save(employee);

            // Return the photo path
            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload photo: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error uploading photo: " + e.getMessage());
        }
    }

    @Override
    public Page<EmployeeProfile> getAllProfiles(Pageable pageable) {
        return employeeProfileRepository.findAll(pageable);
    }

    @Override
    public Optional<Object> getProfileById(Long id) {
        Optional<EmployeeProfile> profile = employeeProfileRepository.findById(id);
        return profile.map(p -> (Object) p);
    }

    @Override
    public String updateEmployee(Long id, @Valid EmployeeProfile employee, MultipartFile photoFile) {
        try {
            // Get existing employee
            EmployeeProfile existing = employeeProfileRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("EmployeeProfile not found with id: " + id));

            // Update all fields
            existing.setFullName(employee.getFullName());
            existing.setPhoneNumber(employee.getPhoneNumber());
            existing.setDob(employee.getDob());
            existing.setGender(employee.getGender());
            existing.setBloodGroup(employee.getBloodGroup());
            existing.setEmail(employee.getEmail());
            existing.setMaritalStatus(employee.getMaritalStatus());
            existing.setFieldOfStudy(employee.getFieldOfStudy());
            existing.setHighestQualification(employee.getHighestQualification());
            existing.setUniversity(employee.getUniversity());
            existing.setPassingYear(employee.getPassingYear());
            existing.setBankName(employee.getBankName());
            existing.setAccountNumber(employee.getAccountNumber());
            existing.setAccountHolderName(employee.getAccountHolderName());
            existing.setIfscCode(employee.getIfscCode());
            existing.setCurrentAddress(employee.getCurrentAddress());
            existing.setPermanentAddress(employee.getPermanentAddress());
            existing.setDepartment(employee.getDepartment());
            existing.setDesignation(employee.getDesignation());
            existing.setStatus(employee.getStatus());
            existing.setUpdatedAt(LocalDateTime.now());

            String photoPath = existing.getPhoto();

            // Upload photo if provided
            if (photoFile != null && !photoFile.isEmpty()) {
                // Define upload directory
                String uploadDir = "uploads/employee_photos/";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String originalFilename = photoFile.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String fileName = existing.getUserId() + "_" + System.currentTimeMillis() + fileExtension;
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, photoFile.getBytes());
                photoPath = filePath.toString();
                existing.setPhoto(photoPath);
            }

            employeeProfileRepository.save(existing);

            // Return the photo path
            return photoPath;

        } catch (IOException e) {
            throw new RuntimeException("Failed to update employee with photo: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error updating employee: " + e.getMessage());
        }
    }

    @Override
    public void deleteEmployee(Long id) {
        // Hard delete - permanently remove from database
        EmployeeProfile existing = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EmployeeProfile not found with id: " + id));
        employeeProfileRepository.delete(existing);
    }

    @Override
    public void softDeleteEmployee(Long id) {
        // Soft delete - just update status to DELETED
        EmployeeProfile existing = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EmployeeProfile not found with id: " + id));
        existing.setStatus("DELETED");
        existing.setUpdatedAt(LocalDateTime.now());
        employeeProfileRepository.save(existing);
    }

    @Override
    public EmployeeProfile getProfileByUserId(String currentEmplId) {
        EmployeeProfile profile= employeeProfileRepository.findByUserId(currentEmplId)
                //.orElseThrow(() -> new RuntimeException("EmployeeProfile not found with userId: " + currentEmplId));
                .orElse(null);
        return profile;
    }

    public List<CelebrationDto> getTodayCelebrations(){
        List<EmployeeProfile> employees = employeeProfileRepository.findAll();

        List<CelebrationDto> celebrations = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (EmployeeProfile employee : employees) {

            // Birthday
            if (employee.getDob() != null &&
                    employee.getDob().getMonth() == today.getMonth() &&
                    employee.getDob().getDayOfMonth() == today.getDayOfMonth()) {

                CelebrationDto dto = new CelebrationDto();
                dto.setUserId(employee.getUserId());
                dto.setFullName(employee.getFullName());
                dto.setDob(employee.getDob());
                dto.setPhoto(employee.getPhoto());
                dto.setType("Birthday");

                celebrations.add(dto);
            }

            // Work Anniversary
            if (employee.getRegisteredAt() != null &&
                    employee.getRegisteredAt().getMonth() == today.getMonth() &&
                    employee.getRegisteredAt().getDayOfMonth() == today.getDayOfMonth()) {

                CelebrationDto dto = new CelebrationDto();
                dto.setUserId(employee.getUserId());
                dto.setFullName(employee.getFullName());
                dto.setRegisteredAt(employee.getRegisteredAt());
                dto.setPhoto(employee.getPhoto());

                int years = Period.between(employee.getRegisteredAt().toLocalDate(), today).getYears();
                dto.setType("Anniversary");

                celebrations.add(dto);
            }
        }

        return celebrations;
    }
}