//package com.hrms.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class PayrollSummary {
//    private String department;
//    private Integer employees;
//    private Double payrollCost;
//}






package EmployeeManagementSystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "payroll_records")
@Data
public class PayrollRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private String month;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Double grossSalary;

    @Column(nullable = false)
    private Double deductions;

    @Column(nullable = false)
    private Double netSalary;

    private String paymentStatus;

    @Column(name = "generated_date")
    private LocalDate generatedDate;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Lob
    @Column(name = "notes")
    private String notes;

    @PrePersist
    protected void onCreate() {
        if (generatedDate == null) {
            generatedDate = LocalDate.now();
        }
        if (paymentStatus == null) {
            paymentStatus = "Processing";
        }
    }
}