//package EmployeeManagementSystem.service;
//
//import EmployeeManagementSystem.entity.Timesheet;
//
//import java.util.List;
//
//public interface TimesheetService {
//     Timesheet saveTimesheet(Timesheet timesheet);
//     List<Timesheet> getTimesheetsByEmployee(String employeeId);
//     List<Timesheet> getAllTimesheet();
//     void updateTimesheetStatus(Long id,String action);
//
//     Timesheet getById(Long id);
//     List<Timesheet> getTimesheetsByEmployeeId(String employeeId);
//}














package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.EmployeeTimesheetDTO;
import EmployeeManagementSystem.entity.Timesheet;

import java.util.List;

public interface TimesheetService {

     /**
      * Save Employee Timesheet
      */
     Timesheet saveTimesheet(Timesheet timesheet);

     /**
      * Get Timesheets of Employee
      */
     List<Timesheet> getTimesheetsByEmployee(String employeeId);

     /**
      * Get All Timesheets
      */
     List<Timesheet> getAllTimesheet();

     /**
      * Get Unique Employees (One Row Per Employee)
      */
     List<EmployeeTimesheetDTO> getAllEmployees();

     /**
      * Approve / Reject Timesheet
      */
     void updateTimesheetStatus(Long id, String action);

     /**
      * Get Timesheet By Id
      */
     Timesheet getById(Long id);

     /**
      * Get All Timesheets of Selected Employee
      */
     List<Timesheet> getTimesheetsByEmployeeId(String employeeId);
}