package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.Timesheet;
import EmployeeManagementSystem.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimesheetServiceImpl implements TimesheetService{
    private final TimesheetRepository repository;
    public Timesheet saveTimesheet(Timesheet timesheet){
        return repository.save(timesheet);
    }
    public List<Timesheet> getTimesheetsByEmployee(String employeeId) {
        return repository.findByEmployeeId(employeeId);
    }
    public List<Timesheet> getAllTimesheet(){
        return repository.findAll();
    }
    public void updateTimesheetStatus(Long id,String action){
        Timesheet sheet=repository.findById(id).orElseThrow(()->new RuntimeException("Timsheet not found"));
        if ("approve".equalsIgnoreCase(action)){
            sheet.setStatus("APPROVED");
        } else if ("reject".equalsIgnoreCase(action)) {
            sheet.setStatus("REJECTED");
        }
        repository.save(sheet);
    }
}
