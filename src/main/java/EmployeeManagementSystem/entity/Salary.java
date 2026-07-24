//package EmployeeManagementSystem.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.LocalDate;
//
//@Getter
//@Setter
//@Entity
//@Table(name = "salary")
//public class Salary {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "employee_id")
//    private Employee employee;
//
//    @Column(name = "employee_code")
//    private String employeeId;
//
//    private String employeeName;
//    private String department;
//    private String designation;
//
//    private Double basicSalary;
//    private Double hra;
//    private Double allowance;
//    private Double bonus;
//    private Double deductions;
//
//    private Double grossSalary;
//    private Double netSalary;
//
//    private String paymentStatus;
//
//    private String month;
//    private Integer year;
//
//    private LocalDate paymentDate;
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
@Data
@Table(name = "salary")
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "employee_code")
    private String employeeId;

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
