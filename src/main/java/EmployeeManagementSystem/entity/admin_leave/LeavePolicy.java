package EmployeeManagementSystem.entity.admin_leave;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeavePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Min(0) private Integer accrualRate; // days per year
    @Min(0) private Integer maxCarryOver;
    private Boolean requiresApproval = true;

    @NotNull(message = "Leave type is required")
    @ManyToOne
    private LeaveType leaveType;

    // additional: minNoticeDays, etc.
}
