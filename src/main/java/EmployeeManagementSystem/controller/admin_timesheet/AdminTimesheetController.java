//package EmployeeManagementSystem.controller.admin_timesheet;
//
//import EmployeeManagementSystem.entity.Timesheet;
//import EmployeeManagementSystem.service.TimesheetService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//@RequestMapping("/admin/timesheets")
//@RequiredArgsConstructor
//public class AdminTimesheetController {
//
//    private final TimesheetService timesheetService;
//
//    // =====================================================
//    // Display All Employee Timesheets
//    // URL : /admin/timesheets/all
//    // =====================================================
//    @GetMapping("/all")
//    public String showAllTimesheets(Model model) {
//
//        System.out.println("========== Admin Timesheet ==========");
//
//        List<Timesheet> timesheets = timesheetService.getAllTimesheet();
//
//        System.out.println("Total Timesheets : " + timesheets.size());
//
//        model.addAttribute("timesheets", timesheets);
//
//        return "admin/timesheet-management/all-timesheet";
//    }
//
//    // =====================================================
//    // View All Timesheets of a Particular Employee
//    // URL : /admin/timesheets/view/EMP0004
//    // =====================================================
//    @GetMapping("/view/{employeeId}")
//    public String viewEmployeeTimesheets(@PathVariable("employeeId") String employeeId, Model model) {
//        System.out.println("======================================");
//        System.out.println("Employee ID : " + employeeId);
//
//        List<Timesheet> timesheets = timesheetService.getTimesheetsByEmployeeId(employeeId);
//
//        System.out.println("Total Records : " + timesheets.size());
//
//        if (timesheets.isEmpty()) {
//            model.addAttribute("message", "No timesheets found for employee : " + employeeId);
//            model.addAttribute("employeeId", employeeId);
//            model.addAttribute("timesheets", new ArrayList<>());
//        } else {
//            // Format dates if needed
//            for (Timesheet sheet : timesheets) {
//                // Format date if you have date formatting logic
//                // sheet.setFormattedDate(sheet.getDate().toString());
//            }
//            model.addAttribute("timesheets", timesheets);
//        }
//
//        return "admin/timesheet-management/view-timesheet";
//    }
//}









package EmployeeManagementSystem.controller.admin_timesheet;

import EmployeeManagementSystem.dto.EmployeeTimesheetDTO;
import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.Timesheet;
import EmployeeManagementSystem.service.TimesheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/timesheets")
@RequiredArgsConstructor
public class AdminTimesheetController {

    private final TimesheetService timesheetService;

    // =====================================================
    // Display Employee List (One Row Per Employee)
    // URL : /admin/timesheets/all
    // =====================================================
    @GetMapping("/all")
    public String showAllEmployees(Model model) {

        System.out.println("========== Employee Timesheet Status ==========");

        // Get all employees with their timesheet status
        List<EmployeeTimesheetDTO> allEmployees = timesheetService.getAllEmployeesWithZeroIncluded();
        List<EmployeeTimesheetDTO> all_employee=timesheetService.getAllEmployees();
        System.out.println("Total Employees : " + allEmployees.size());

        // Split employees into two lists
        List<EmployeeTimesheetDTO> submittedEmployees = all_employee.stream()
                .filter(emp -> emp.getTimesheetCount() > 0)
                .collect(Collectors.toList());

        List<EmployeeTimesheetDTO> pendingEmployees = allEmployees.stream()
                .filter(emp -> emp.getTimesheetCount() == 0)
                .collect(Collectors.toList());

        System.out.println("Employees with Timesheet : " + submittedEmployees.size());
        System.out.println("Employees without Timesheet : " + pendingEmployees.size());

        model.addAttribute("submittedEmployees", submittedEmployees);
        model.addAttribute("pendingEmployees", pendingEmployees);

        return "admin/timesheet-management/all-timesheet";
    }
    @GetMapping("/view")
    public String viewTimesheet(Model model){
        List<EmployeeTimesheetDTO> allTimesheet=timesheetService.getAllEmployees();
        List<EmployeeTimesheetDTO> submittedEmployees=allTimesheet.stream()
                .filter(emp -> emp.getTimesheetCount() > 0)
                .collect(Collectors.toList());
        model.addAttribute("submittedEmployees",submittedEmployees);
        return "admin/timesheet-management/view-employeeTimesheet";
    }
    // =====================================================
    // Display All Timesheets of Selected Employee
    // URL : /admin/timesheets/view/EMP0004
    // =====================================================
    @GetMapping("/view/{employeeId}")
    public String viewEmployeeTimesheets(@PathVariable String employeeId,
                                         Model model) {

        System.out.println("==================================");
        System.out.println("Employee ID : " + employeeId);

        List<Timesheet> timesheets =
                timesheetService.getTimesheetsByEmployeeId(employeeId);

        System.out.println("Total Records : " + timesheets.size());

        model.addAttribute("employeeId", employeeId);
        model.addAttribute("timesheets", timesheets);

        if (!timesheets.isEmpty()) {
            model.addAttribute("employeeName",
                    timesheets.get(0).getEmployeeName());
        }

        return "admin/timesheet-management/view-timesheet";
    }

    @PostMapping("/approve/{id}")
    public String approveTimesheet(@PathVariable Long id) {

        timesheetService.updateTimesheetStatus(id, "approve");

        return "redirect:/admin/timesheets/view/" +
                timesheetService.getById(id).getEmployeeId();
    }

    @PostMapping("/reject/{id}")
    public String rejectTimesheet(@PathVariable Long id) {

        timesheetService.updateTimesheetStatus(id, "reject");

        return "redirect:/admin/timesheets/view/" +
                timesheetService.getById(id).getEmployeeId();
    }
}