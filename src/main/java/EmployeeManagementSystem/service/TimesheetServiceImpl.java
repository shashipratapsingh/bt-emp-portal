//package EmployeeManagementSystem.service;
//
//import EmployeeManagementSystem.entity.Timesheet;
//import EmployeeManagementSystem.repository.TimesheetRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class TimesheetServiceImpl implements TimesheetService{
//    private final TimesheetRepository repository;
//    public Timesheet saveTimesheet(Timesheet timesheet){
//        return repository.save(timesheet);
//    }
//    public List<Timesheet> getTimesheetsByEmployee(String employeeId) {
//        return repository.findByEmployeeId(employeeId);
//    }
//    public List<Timesheet> getAllTimesheet(){
//        return repository.findAll();
//    }
//    public void updateTimesheetStatus(Long id,String action){
//        Timesheet sheet=repository.findById(id).orElseThrow(()->new RuntimeException("Timesheet not found"));
//        if ("approve".equalsIgnoreCase(action)){
//            sheet.setStatus("APPROVED");
//        } else if ("reject".equalsIgnoreCase(action)) {
//            sheet.setStatus("REJECTED");
//        }
//        repository.save(sheet);
//    }
//
//    @Override
//    public Timesheet getById(Long id) {
//
//        return repository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Timesheet not found with id : " + id)
//                );
//    }
//
//    @Override
//    public List<Timesheet> getTimesheetsByEmployeeId(String employeeId) {
//        return repository.findByEmployeeIdOrderByDateDesc(employeeId);
//    }
//}















package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.EmployeeTimesheetDTO;
import EmployeeManagementSystem.entity.Timesheet;
import EmployeeManagementSystem.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimesheetServiceImpl implements TimesheetService {

    private final TimesheetRepository repository;

    @Override
    public Timesheet saveTimesheet(Timesheet timesheet) {
        return repository.save(timesheet);
    }

    @Override
    public List<Timesheet> getTimesheetsByEmployee(String employeeId) {
        return repository.findByEmployeeId(employeeId);
    }

    @Override
    public List<Timesheet> getAllTimesheet() {
        return repository.findAll();
    }

    /**
     * Returns one record per employee
     * (Used in Admin Employee List Page)
     */
    @Override
    public List<EmployeeTimesheetDTO> getAllEmployees() {
        return repository.getAllEmployees();
    }

    @Override
    public void updateTimesheetStatus(Long id, String action) {

        Timesheet sheet = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Timesheet not found"));

        if ("approve".equalsIgnoreCase(action)) {
            sheet.setStatus("APPROVED");
        } else if ("reject".equalsIgnoreCase(action)) {
            sheet.setStatus("REJECTED");
        }

        repository.save(sheet);
    }

    @Override
    public Timesheet getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Timesheet not found with id : " + id));
    }

    /**
     * Returns all timesheets of a selected employee
     */
    @Override
    public List<Timesheet> getTimesheetsByEmployeeId(String employeeId) {
        return repository.findByEmployeeIdOrderByDateDesc(employeeId);
    }
}
