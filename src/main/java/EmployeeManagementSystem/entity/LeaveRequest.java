//package EmployeeManagementSystem.entity;
//
//import EmployeeManagementSystem.enums.LeaveStatus;
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDate;
//
//
//@Entity
//@Data
//public class LeaveRequest {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//    private String employeeId;
//    private String employeeName;
//    private LocalDate startDate;
//    private LocalDate endDate;
//    private String leaveType;
//    private String reason;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private LeaveStatus status = LeaveStatus.PENDING;
//
//}










package EmployeeManagementSystem.entity;

import EmployeeManagementSystem.enums.LeaveStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "leave_request")
@Data
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeId;

    private String employeeName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String leaveType;

    @Column(length = 1000)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status = LeaveStatus.PENDING;


    public long getTotalDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
