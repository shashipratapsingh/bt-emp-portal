package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.dto.DashboardStatsDTO;
import EmployeeManagementSystem.entity.*;
import EmployeeManagementSystem.repository.*;
import EmployeeManagementSystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeProfileRepository employeeProfileRepository;   // already present

    @Autowired
    private EmployeeProfileService employeeProfileService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RegisterEmployeeRepository registerEmployeeRepository;

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private LeaveService leaveService;

    private final ActivityService activityService;
    private final DashboardService dashboardService;
    private final DepartmentServiceImpl departmentService;

    public AdminController(ActivityService activityService,
                           DashboardService dashboardService,
                           DepartmentServiceImpl departmentService) {
        this.activityService = activityService;
        this.dashboardService = dashboardService;
        this.departmentService = departmentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size) {

        // -------- FIX: Get logged-in user's full name from EmployeeProfile --------
        String userName = "Guest";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {
            // Principal is the user ID (e.g., "EMP0001")
            String userId = authentication.getName();  // or getPrincipal().toString()
            if (userId != null && !userId.isEmpty()) {
                // Fetch the employee profile by userId
                RegisterEmployee profile = registerEmployeeRepository.findByUserId(userId);
                if (profile != null && profile.getName() != null && !profile.getName().isEmpty()) {
                    userName = profile.getName();   // e.g., "Sujit kumar"
                } else {
                    userName = userId;   // fallback to user ID if name not found
                }
            }
        }
        model.addAttribute("loggedInUser", userName);
        // -----------------------------------------------------

        // Add currentPage attribute for sidebar to highlight active menu
        model.addAttribute("currentPage", "dashboard");

        try {
            // Try to get data from service
            DashboardStatsDTO stats = dashboardService.getDashboardStats();

            if (stats != null) {
                model.addAttribute("stats", stats);
                model.addAttribute("totalEmployees", stats.getTotalEmployees());
                model.addAttribute("activeEmployees", stats.getTotalEmployees() - 15); // Calculate active employees
                model.addAttribute("departments", stats.getDepartments());
                model.addAttribute("attendanceRate", stats.getAttendanceRate());
                model.addAttribute("payroll", stats.getPayroll());
                model.addAttribute("joined", stats.getJoined());
                model.addAttribute("left", stats.getLeft());
                model.addAttribute("attendanceTrend", stats.getAttendanceTrend());

                int newJoinees = (stats.getJoined() != null)
                        ? stats.getJoined().stream().mapToInt(Integer::intValue).sum()
                        : 0;
                model.addAttribute("newJoinees", newJoinees);
            } else {
                // If stats is null, use fallback data
                addFallbackData(model);
            }

            model.addAttribute("vacancies", 18);
            model.addAttribute("pendingLeaves", 11);

            // Get upcoming birthdays
            List<?> birthdays = dashboardService.getUpcomingBirthdaysDTO();
            model.addAttribute("upcomingBirthdays", birthdays != null ? birthdays : Collections.emptyList());

            // Get upcoming anniversaries
            List<?> anniversaries = dashboardService.getUpcomingAnniversariesDTO();
            model.addAttribute("anniversaries", anniversaries != null ? anniversaries : Collections.emptyList());

            log.info("Birthdays count: {}", birthdays != null ? birthdays.size() : 0);
            log.info("Anniversaries count: {}", anniversaries != null ? anniversaries.size() : 0);

            // Get recent activities
            Page<Activity> activityPage = activityService.getActivities(page, size);
            model.addAttribute("activities", activityPage.getContent());
            model.addAttribute("pageNumber", page);
            model.addAttribute("totalPages", activityPage.getTotalPages());
            model.addAttribute("hasNext", activityPage.hasNext());
            model.addAttribute("hasPrevious", activityPage.hasPrevious());

            // --- STATIC SAMPLE DATA FOR DISPLAY ---

            // Employee Status - This will show in the status section
            List<StatusStat> employeeStatus = Arrays.asList(
                    new StatusStat("Active", 120, 80.0),
                    new StatusStat("On Notice Period", 10, 6.7),
                    new StatusStat("Resigned", 8, 5.3),
                    new StatusStat("Terminated", 5, 3.3)
            );
            model.addAttribute("employeeStatus", employeeStatus);

            // Department Stats
            List<DepartmentStat> departmentStats = Arrays.asList(
                    new DepartmentStat("Information Technology", 45, 30.0),
                    new DepartmentStat("Human Resources", 20, 13.3),
                    new DepartmentStat("Finance", 15, 10.0),
                    new DepartmentStat("Marketing", 25, 16.7),
                    new DepartmentStat("Operations", 30, 20.0),
                    new DepartmentStat("Sales", 15, 10.0),
                    new DepartmentStat("Others", 5, 3.3)
            );
            model.addAttribute("departmentStats", departmentStats);

            // Recent Employees
            List<RecentEmployee> recentEmployees = Arrays.asList(
                    new RecentEmployee("Priya Sharma", "HR Executive"),
                    new RecentEmployee("Rohan Patel", "Software Engineer"),
                    new RecentEmployee("Neha Singh", "Accountant"),
                    new RecentEmployee("Amit Patel", "Marketing Executive"),
                    new RecentEmployee("Sneh Verma", "UI/UX Designer")
            );
            model.addAttribute("recentEmployees", recentEmployees);

            // Employee List for Table
            List<EmployeeTableRow> employeeList = Arrays.asList(
                    new EmployeeTableRow("EMP001", "Rahul Sharma", "IT", "Software Engineer", "rahul.sharma@email.com", "9876543210", "Active"),
                    new EmployeeTableRow("EMP002", "Neha Singh", "Finance", "Accountant", "neha.singh@email.com", "9876543211", "Active"),
                    new EmployeeTableRow("EMP003", "Amit Patel", "Marketing", "Marketing Executive", "amit.patel@email.com", "9876543212", "On Notice"),
                    new EmployeeTableRow("EMP004", "Pooja Verma", "HR", "HR Executive", "pooja.verma@email.com", "9876543213", "Active"),
                    new EmployeeTableRow("EMP005", "Vikram Singh", "IT", "Team Lead", "vikram.singh@email.com", "9876543214", "Active")
            );
            model.addAttribute("employeeList", employeeList);

            // Recent Activities (for display)
            List<ActivityDTO> recentActivities = Arrays.asList(
                    new ActivityDTO("<strong>Priya Sharma</strong> has been added as new employee", "#2a6df4", "2 min ago"),
                    new ActivityDTO("<strong>Neha Singh</strong> applied for leave", "#e67e22", "15 min ago"),
                    new ActivityDTO("<strong>Amit Kumar</strong> WFH request approved", "#0f973d", "1 hour ago"),
                    new ActivityDTO("<strong>Pooja Verma</strong> document uploaded", "#2a6df4", "3 hours ago"),
                    new ActivityDTO("<strong>Rohan Patel</strong>'s profile has been updated", "#8a9bb5", "5 hours ago")
            );
            model.addAttribute("recentActivities", recentActivities);

        } catch (Exception e) {
            log.error("Error loading dashboard: ", e);
            model.addAttribute("error", e.getMessage());

            // Add fallback data if service fails
            addFallbackData(model);
        }

        return "admin/dashboard";
    }

    private void addFallbackData(Model model) {
        // Fallback data with realistic numbers
        model.addAttribute("totalEmployees", 150);
        model.addAttribute("activeEmployees", 120);
        model.addAttribute("newJoinees", 12);
        model.addAttribute("vacancies", 18);
        model.addAttribute("pendingLeaves", 11);
        model.addAttribute("departments", 8);
        model.addAttribute("attendanceRate", 91.0);
        model.addAttribute("payroll", 75000);
    }

    @GetMapping("/employees")
    public String showAllEmployees(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        Employee employee = new Employee();
        employee.setDepartment(new Department());
        employee.setSalaryDetails(new Salary());
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("employees", employees);
        model.addAttribute("employee", employee);
        model.addAttribute("departments", departments);
        return "admin/employees";
    }

    @GetMapping("/all-profiles-as-employees")
    public String showEmployeesFromProfile(Model model,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "id") String sortBy,
                                           @RequestParam(defaultValue = "asc") String sortDir) {
        System.out.println("===== CONTROLLER HIT =====");

        // Create Pageable with sorting
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // Get paginated employee profiles
        Page<EmployeeProfile> profilePage = employeeProfileService.getAllProfiles(pageable);

        model.addAttribute("profiles", profilePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", profilePage.getTotalPages());
        model.addAttribute("totalItems", profilePage.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        return "admin/all-employees";
    }

    // ===== INNER CLASSES =====
    public static class DepartmentStat {
        public String name;
        public int count;
        public double percentage;
        public DepartmentStat(String name, int count, double percentage) {
            this.name = name;
            this.count = count;
            this.percentage = percentage;
        }
        public String getName() { return name; }
        public int getCount() { return count; }
        public double getPercentage() { return percentage; }
    }

    public static class StatusStat {
        public String name;
        public int count;
        public double percentage;
        public StatusStat(String name, int count, double percentage) {
            this.name = name;
            this.count = count;
            this.percentage = percentage;
        }
        public String getName() { return name; }
        public int getCount() { return count; }
        public double getPercentage() { return percentage; }
    }

    public static class RecentEmployee {
        public String name;
        public String designation;
        public RecentEmployee(String name, String designation) {
            this.name = name;
            this.designation = designation;
        }
        public String getName() { return name; }
        public String getDesignation() { return designation; }
    }

    public static class EmployeeTableRow {
        public String employeeId;
        public String name;
        public String department;
        public String designation;
        public String email;
        public String phone;
        public String status;
        public EmployeeTableRow(String employeeId, String name, String department, String designation, String email, String phone, String status) {
            this.employeeId = employeeId;
            this.name = name;
            this.department = department;
            this.designation = designation;
            this.email = email;
            this.phone = phone;
            this.status = status;
        }
        public String getEmployeeId() { return employeeId; }
        public String getName() { return name; }
        public String getDepartment() { return department; }
        public String getDesignation() { return designation; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getStatus() { return status; }
    }

    public static class ActivityDTO {
        public String text;
        public String color;
        public String time;
        public ActivityDTO(String text, String color, String time) {
            this.text = text;
            this.color = color;
            this.time = time;
        }
        public String getText() { return text; }
        public String getColor() { return color; }
        public String getTime() { return time; }
    }
}