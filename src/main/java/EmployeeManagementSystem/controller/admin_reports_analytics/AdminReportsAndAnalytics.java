package EmployeeManagementSystem.controller.admin_reports_analytics;

import EmployeeManagementSystem.dto.AttendanceRecordDTO;
import EmployeeManagementSystem.dto.EmployeeDetailsDTO;
import EmployeeManagementSystem.entity.EmployeeProfile;
import EmployeeManagementSystem.service.AttendanceService;
import EmployeeManagementSystem.service.AttendanceSummary;
import EmployeeManagementSystem.service.EmployeeProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/reports-and-analytics")
public class AdminReportsAndAnalytics {

    private final EmployeeProfileService profileService;
    private final AttendanceService attendanceService;

    public AdminReportsAndAnalytics(EmployeeProfileService profileService,
                                    AttendanceService attendanceService) {
        this.profileService = profileService;
        this.attendanceService = attendanceService;
    }

    // ===== Employee List with filters =====
    @GetMapping("/employee-reports")
    public String employeeReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));

        Page<EmployeeProfile> profilePage = profileService.filterEmployees(keyword, department, status, pageable);

        model.addAttribute("profiles", profilePage.getContent());
        model.addAttribute("currentPage", profilePage.getNumber());
        model.addAttribute("totalPages", profilePage.getTotalPages());
        model.addAttribute("totalItems", profilePage.getTotalElements());

        model.addAttribute("keyword", keyword);
        model.addAttribute("department", department);
        model.addAttribute("status", status);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        model.addAttribute("departments", profileService.getAllDepartments());
        model.addAttribute("pageTitle", "Employee Reports");

        return "admin/reports-and-analytics/employee-records";
    }

    // ===== Employee Detail =====
    @GetMapping("/employee-detail/{id}")
    public String employeeDetail(@PathVariable Long id, Model model) {
        EmployeeDetailsDTO details = profileService.getEmployeeDetails(id);
        model.addAttribute("details", details);
        model.addAttribute("profile", details.getProfile());
        model.addAttribute("pageTitle", "Employee Profile - " + details.getProfile().getFullName());
        return "admin/reports-and-analytics/employee-details";
    }

    // ===== Attendance Reports =====
    @GetMapping("/attendance-reports")
    public String attendanceReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            Model model) {

        // 1. Fetch paginated attendance records with filters
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<AttendanceRecordDTO> recordPage = attendanceService.getFilteredAttendance(
                fromDate, toDate, department, status, keyword, pageable);

        // 2. Summary statistics for today (or the selected date range)
        //    For simplicity, we use today's stats. You can compute based on filters if needed.
        LocalDate today = LocalDate.now();
        AttendanceSummary summary = attendanceService.getAttendanceSummary(today, department);

        // 3. List of departments for dropdown
        List<String> departments = profileService.getAllDepartments();

        // 4. Populate model
        model.addAttribute("attendanceRecords", recordPage.getContent());
        model.addAttribute("currentPage", recordPage.getNumber());
        model.addAttribute("totalPages", recordPage.getTotalPages());
        model.addAttribute("totalRecords", recordPage.getTotalElements());
        model.addAttribute("size", size);

        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("department", department);
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword);

        // Summary stats
        model.addAttribute("totalEmployees", summary.getTotalEmployees());
        model.addAttribute("presentToday", summary.getPresent());
        model.addAttribute("absentToday", summary.getAbsent());
        model.addAttribute("lateToday", summary.getLate());
        model.addAttribute("onLeaveToday", summary.getOnLeave());
        model.addAttribute("attendancePercentage", summary.getAttendancePercentage());

        model.addAttribute("departments", departments);
        model.addAttribute("pageTitle", "Attendance Reports");

        return "admin/reports-and-analytics/attendance-reports";
    }

    // ===== Other report endpoints =====
    @GetMapping("/payroll-reports")
    public String payrollReports() {
        return "admin/reports-and-analytics/payroll-reports";
    }

    @GetMapping("/recruitment-reports")
    public String recruitmentReports() {
        return "admin/reports-and-analytics/recruitment-reports";
    }

    @GetMapping("/project-reports")
    public String projectReports() {
        return "admin/reports-and-analytics/project-reports";
    }
}