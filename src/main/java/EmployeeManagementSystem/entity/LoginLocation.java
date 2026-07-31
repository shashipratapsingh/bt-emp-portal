package EmployeeManagementSystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String employeeId;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    // Distance from office in meters
    @Column(nullable = false)
    private Double distanceFromOffice;

    @Column(nullable = false)
    private String workMode;

    @Column(nullable = false)
    private String loginStatus;

    @Column(nullable = false)
    private LocalDate loginDate;

    @Column(nullable = false)
    private LocalDateTime loginTime;
}