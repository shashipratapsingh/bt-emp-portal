package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.Timesheet;

import java.util.List;

public interface TimesheetService {
     Timesheet saveTimesheet(Timesheet timesheet);
     List<Timesheet> getTimesheetsByEmployee(String employeeId);
     List<Timesheet> getAllTimesheet();
     void updateTimesheetStatus(Long id,String action);
}
