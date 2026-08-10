package EmployeeManagementSystem.entity;

import EmployeeManagementSystem.entity.admin_salary.SalaryStructure;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "salary")
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "employee_profile_id")
    private Long employeeProfileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_profile_id", referencedColumnName = "id", insertable = false, updatable = false)
    private EmployeeProfile employeeProfile;

    @Column(name = "employee_code")
    private String employeeCode;

    private String employeeName;
    private String department;
    private String designation;

    private Double basicSalary;
    private Double hra;
    private Double allowance;
    private Double bonus;
    private Double deductions;

    private Double grossSalary;
    private Double netSalary;

    private String paymentStatus;

    private String month;
    private Integer year;

    private LocalDate paymentDate;
}