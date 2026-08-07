package EmployeeManagementSystem.dto;

public class EmployeeTimesheetDTO {

    private String employeeId;
    private String employeeName;

    public EmployeeTimesheetDTO(String employeeId,
                                String employeeName) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }
}