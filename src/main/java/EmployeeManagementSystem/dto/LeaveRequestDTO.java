package EmployeeManagementSystem.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// LeaveRequestDTO.java – used when employee applies for leave
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestDTO {
    @NotBlank(message = "Leave type name is required")
    private String leaveTypeName;

    @NotNull @FutureOrPresent
    private LocalDate startDate;

    @NotNull @FutureOrPresent
    private LocalDate endDate;

    @Size(max = 1000)
    private String reason;
    // getters/setters
}

