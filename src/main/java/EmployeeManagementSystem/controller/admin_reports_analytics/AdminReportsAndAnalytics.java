// AdminReportsAndAnalytics.java
package EmployeeManagementSystem.controller.admin_reports_analytics;

import EmployeeManagementSystem.dto.EmployeeReportDTO;
import EmployeeManagementSystem.entity.Department;
import EmployeeManagementSystem.service.EmployeeReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/reports-and-analytics")
public class AdminReportsAndAnalytics {

    @Autowired
    private EmployeeReportService employeeReportService;

    @GetMapping("/employee-reports")
    public String getEmployeeReports(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        if (status != null && status.trim().isEmpty()) {
            status = null;
        }
        if (departmentId != null && departmentId == 0) {
            departmentId = null;
        }

        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EmployeeReportDTO> employeePage = employeeReportService.getEmployeeReports(
                keyword, departmentId, status, pageable);

        List<Department> departments = employeeReportService.getAllDepartments();

        model.addAttribute("employeeReports", employeePage.getContent());
        model.addAttribute("totalItems", employeePage.getTotalElements());
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("currentPage", employeePage.getNumber());
        model.addAttribute("keyword", keyword);
        model.addAttribute("departmentId", departmentId);
        model.addAttribute("status", status);
        model.addAttribute("departments", departments);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("pageTitle", "Employee Reports");

        return "admin/reports-and-analytics/employee-records";
    }

    // AdminReportsAndAnalytics.java
    @GetMapping("/employee-details/{profileId}")
    public String getEmployeeDetail(@PathVariable Long profileId, Model model) {
        System.out.println("=== EMPLOYEE DETAIL REQUEST ===");
        System.out.println("Profile ID: " + profileId);

        try {
            EmployeeReportDTO employeeDetail = employeeReportService.getEmployeeDetail(profileId);
            model.addAttribute("employee", employeeDetail);
            model.addAttribute("pageTitle", "Employee Details");
            return "admin/reports-and-analytics/employee-details";
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            model.addAttribute("errorMessage", "Employee not found with ID: " + profileId);
            return "redirect:/admin/reports-and-analytics/employee-reports";
        }
    }

    // Alternative mapping using id
    @GetMapping("/employee-detail/{id}")
    public String getEmployeeDetailById(@PathVariable Long id, Model model) {
        return getEmployeeDetail(id, model);
    }

}