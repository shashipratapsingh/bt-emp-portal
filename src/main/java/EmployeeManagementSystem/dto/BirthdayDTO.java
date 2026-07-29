package EmployeeManagementSystem.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Data
public class BirthdayDTO {

    private String name;
    private LocalDate dob;

    private String department;

    private long remainingDays;

    private LocalDate nextDate;
}