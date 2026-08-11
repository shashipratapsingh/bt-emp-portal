package EmployeeManagementSystem.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EmployeeTimesheetDTO {

    private String employeeId;
    private String employeeName;
    private int timesheetCount;
    public EmployeeTimesheetDTO(String employeeId, String employeeName, Long timesheetCount) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.timesheetCount = timesheetCount.intValue();
    }

}