
// EmployeeProfile.java
package EmployeeManagementSystem.entity;

import EmployeeManagementSystem.entity.admin_salary.SalaryStructure;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    // One-to-One with Employee (existing)
    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Employee employee;

    // ===== DIRECT MAPPING TO SALARY STRUCTURE =====
    // One-to-One with SalaryStructure using employee_profile_id
    @OneToOne(mappedBy = "employeeProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SalaryStructure salaryStructure;

    // ===== DIRECT MAPPING TO PROJECTS =====
    // One-to-Many with Projects using employee_profile_id
    @OneToMany(mappedBy = "employeeProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>();

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull(message = "Date of birth is required")
    @Column(name = "dob")
    private LocalDate dob;

    private String gender;
    private String bloodGroup;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "field_of_study")
    private String fieldOfStudy;

    @Column(name = "highest_qualification")
    private String highestQualification;

    private String university;

    @Column(name = "passing_year")
    private String passingYear;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_holder_name")
    private String accountHolderName;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "current_address", columnDefinition = "TEXT")
    private String currentAddress;

    @Column(name = "permanent_address", columnDefinition = "TEXT")
    private String permanentAddress;

    @Column(columnDefinition = "TEXT")
    private String photo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "status")
    private String status = "ACTIVE";

    // ===== NEW FIELDS =====
    @Column(name = "department")
    private String department;

    @Column(name = "designation")
    private String designation;

    @Column(name = "password")
    private String password;

    @Column(name = "email_sent")
    private boolean emailSent = false;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Transient
    private String confirmEmail;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
        if (registeredAt == null) {
            registeredAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}