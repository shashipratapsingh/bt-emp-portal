package EmployeeManagementSystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "wishes")
@Data
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderUserId;   // Wish bhejne wale ka Employee ID (e.g., BT001)
    private String receiverUserId; // Wish receive karne wale ka Employee ID

    private String wishType;       // "Birthday" ya "Anniversary"
    private String message;        // e.g., "Happy Birthday!"

    private LocalDateTime createdAt = LocalDateTime.now();
}
