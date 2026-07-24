package EmployeeManagementSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AnniversaryDTO {

    private String name;

    // ADD THIS (you were missing it in UI)
    private LocalDate joiningDate;

    private long remainingDays;
    private LocalDate nextDate;
}