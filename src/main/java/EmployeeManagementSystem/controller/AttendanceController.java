package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.entity.Attendance;
import EmployeeManagementSystem.entity.AttendanceTracking;
import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.service.AttendanceService;
import EmployeeManagementSystem.service.AttendanceTrackingService;
import EmployeeManagementSystem.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employee/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EmployeeService employeeService;
    private final AttendanceTrackingService attendanceTrackingService;

    @GetMapping
    public String getAllAttendance(Model model) {

        model.addAttribute(
                "attendanceList",
                attendanceService.getAllAttendance()
        );

        return "attendance-list";
    }

    @GetMapping("/add")
    public String showAttendanceForm(Model model) {

        model.addAttribute("attendance", new Attendance());

        List<Employee> employees = employeeService.getAllEmployeesList();

        model.addAttribute("employees", employees);

        System.out.println("Employees Found = " + employees.size());

        return "attendance-form";
    }

    @PostMapping("/save")
    public String saveAttendance(
            @ModelAttribute Attendance attendance,
            @RequestParam("employeeId") Long employeeId) {

        Employee employee = employeeService.getEmployeeById(employeeId);

        attendance.setEmployee(employee);

        attendanceService.saveAttendance(attendance);

        return "redirect:/attendance";
    }

    @GetMapping("/delete/{id}")
    public String deleteAttendance(@PathVariable Long id) {

        attendanceService.deleteAttendance(id);

        return "redirect:/attendance";
    }
//    @GetMapping("/today")
//    public List<Attendance> getTodayAttendance() {
//
//        return attendanceService.getTodayAttendance();
//
//    }
//
//    @GetMapping("/wfh")
//    @ResponseBody
//    public List<Attendance> getTodayWFHEmployees() {
//
//        return attendanceService.getTodayWFHEmployees();
//
//    }

    @GetMapping("/signoff-logs")
    public String getSignoffLogs(Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String employeeId = authentication.getName(); // e.g., BT001

        // 1. Database se is employee ke saare attendance records nikalen
        // (Aap apne actual service/repository method ka naam use karein)
        List<AttendanceTracking> logs = attendanceService.getAttendanceLogsByEmployeeId(employeeId);
        System.out.println("Logs Size = " + logs.size());

        model.addAttribute("attendanceLogs", logs);
        model.addAttribute("currentPage", "signoff"); // Sidebar ko active dikhane ke liye

        return "signoff-details"; // Naye HTML page ka naam
    }

}