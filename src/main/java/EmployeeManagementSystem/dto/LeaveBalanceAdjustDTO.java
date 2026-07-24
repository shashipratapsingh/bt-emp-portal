package EmployeeManagementSystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceAdjustDTO {
    @NotNull private Long employeeId;
    @NotNull private Long leaveTypeId;
    @NotNull private Integer year;
    @NotNull private Double delta;
    private String reason;
}