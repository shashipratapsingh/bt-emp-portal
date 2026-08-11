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
import EmployeeManagementSystem.entity.EmployeeProfile;
import EmployeeManagementSystem.entity.Timesheet;
import EmployeeManagementSystem.repository.EmployeeProfileRepository;
import EmployeeManagementSystem.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimesheetServiceImpl implements TimesheetService {

    private final TimesheetRepository repository;
    private final EmployeeProfileRepository employeeProfileRepository;

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
    // Get all employees with their timesheet count
    public List<EmployeeTimesheetDTO> getAllEmployees() {
        List<Timesheet> allTimesheets = repository.findAll();

        // Group by employeeId and count
        Map<String, Long> employeeTimesheetCount = allTimesheets.stream()
                .collect(Collectors.groupingBy(
                        Timesheet::getEmployeeId,
                        Collectors.counting()
                ));

        // Get unique employees with their count
        List<EmployeeTimesheetDTO> employees = new ArrayList<>();

        // Get all unique employee IDs from timesheets
        allTimesheets.stream()
                .map(Timesheet::getEmployeeId)
                .distinct()
                .forEach(empId -> {
                    // Get first timesheet for employee name
                    Timesheet sample = allTimesheets.stream()
                            .filter(t -> t.getEmployeeId().equals(empId))
                            .findFirst()
                            .orElse(null);

                    if (sample != null) {
                        EmployeeTimesheetDTO dto = new EmployeeTimesheetDTO();
                        dto.setEmployeeId(empId);
                        dto.setEmployeeName(sample.getEmployeeName());

                        dto.setTimesheetCount(employeeTimesheetCount.get(empId).intValue());
                        employees.add(dto);
                    }
                });

        // Note: This only includes employees who have at least one timesheet.
        // For employees with zero timesheets, you need to get from Employee table.
        // See alternative approach below.

        return employees;
    }

    // =====================================================
    // METHOD 2: Saare employees (including zero timesheet wale)
    // Yeh "Alternative Approach" hai
    // =====================================================
    public List<EmployeeTimesheetDTO> getAllEmployeesWithZeroIncluded() {

        List<EmployeeProfile> allEmployees = employeeProfileRepository.findAll();
        List<Timesheet> allTimesheets = repository.findAll();

        // Timesheet count by employeeId
        Map<String, Long> timesheetCountMap = allTimesheets.stream()
                .filter(t -> t.getEmployeeId() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getEmployeeId().trim().toUpperCase(),
                        Collectors.counting()
                ));

        List<EmployeeTimesheetDTO> result = new ArrayList<>();

        for (EmployeeProfile emp : allEmployees) {
            EmployeeTimesheetDTO dto = new EmployeeTimesheetDTO();
            dto.setEmployeeId(emp.getUserId());
            dto.setEmployeeName(emp.getFullName());

            String key = emp.getUserId() == null
                    ? ""
                    : emp.getUserId().trim().toUpperCase();

            int count = timesheetCountMap.getOrDefault(key, 0L).intValue();
            dto.setTimesheetCount(count);

            result.add(dto);
        }

        return result;
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
