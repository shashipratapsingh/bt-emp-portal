package EmployeeManagementSystem.service;

import lombok.Data;

@Data
public class AttendanceSummary {
    private long totalEmployees;
    private long present;
    private long absent;
    private long late;
    private long onLeave;
    private int attendancePercentage;   // (present + late) / total * 100
}
