package EmployeeManagementSystem.entity.admin_leave;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leave_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must not exceed 50 characters")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @NotBlank(message = "Code is required")
    @Pattern(
            regexp = "^[A-Z_]+$",
            message = "Code must contain only uppercase letters and underscores"
    )
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @NotNull(message = "Default days are required")
    @Min(value = 0, message = "Default days cannot be negative")
    @Column(name = "default_days", nullable = false)
    private Integer defaultDays;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}