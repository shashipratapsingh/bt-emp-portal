package EmployeeManagementSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class BirthdayDTO {

    private String name;
    private LocalDate dateOfBirth;

    private String department;

    private long remainingDays;

    private LocalDate nextDate;
}