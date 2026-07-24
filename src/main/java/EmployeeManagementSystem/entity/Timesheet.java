//package EmployeeManagementSystem.entity;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import lombok.Data;
//
//import java.time.LocalDate;
//
//@Entity
//@Data
//public class Timesheet {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String employeeId;
//    private String employeeName;
//    private LocalDate date;
//    private String hoursWorked;
//    private String taskDescription;
//    private String workMode;
//    private String status = "PENDING";
//}


package EmployeeManagementSystem.entity;

import jakarta.persistence.*;
        import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "timesheet")
@Data
public class Timesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeId;

    private String employeeName;

    @Column(name = "date")
    private LocalDate date;

    private String hoursWorked;

    @Column(length = 1000)
    private String taskDescription;

    private String workMode;

    private String status = "PENDING";
}