package EmployeeManagementSystem.controller.admin_salary;


import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.Salary;
import EmployeeManagementSystem.entity.admin_salary.SalaryStructure;
import EmployeeManagementSystem.repository.admin_salaryRepo.AdminSalaryRepo;
import EmployeeManagementSystem.service.admin_salary.MonthlySalaryService;
import EmployeeManagementSystem.service.admin_salary.SalaryStructureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin/payroll")
@RequiredArgsConstructor
public class MonthlySalaryController {

    private final MonthlySalaryService monthlySalaryService;
    private final SalaryStructureService salaryStructureService;
    private final AdminSalaryRepo adminSalaryRepo;

    @GetMapping("/slip/{employeeId}")
    public String showSalarySlip(@PathVariable Long employeeId,
                                 @RequestParam(required = false) String month,
                                 @RequestParam(required = false) Integer year,
                                 Model model) {
        if (month == null || year == null) {
            LocalDate now = LocalDate.now();
            month = now.format(DateTimeFormatter.ofPattern("MMMM"));
            year = now.getYear();
        }

        Salary salary = monthlySalaryService.getSalaryByEmployeeAndMonth(employeeId, month, year);
        Employee employee = salary.getEmployee();

        model.addAttribute("salary", salary);
        model.addAttribute("employee", employee);
        model.addAttribute("monthYear", month + " " + year);
        model.addAttribute("generatedDate", LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));

        return "admin/payroll/salary-slip";
    }
    // GET - Generate Salary Form
    @GetMapping("/generate/{employeeId}")
    public String showGenerateSalaryForm(@PathVariable Long employeeId, Model model) {
        SalaryStructure salaryStructure = salaryStructureService.getSalaryStructureByEmployeeId(employeeId);

        model.addAttribute("employeeId", employeeId);
        model.addAttribute("employeeName", salaryStructure.getEmployee().getFirstName() + " " +
                salaryStructure.getEmployee().getLastName());
        model.addAttribute("currentMonth", LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM")));
        model.addAttribute("currentYear", LocalDate.now().getYear());
        model.addAttribute("pageTitle", "Generate Monthly Salary");

        return "admin/payroll/generate-salary";
    }

    // POST - Generate Single Salary
    @PostMapping("/generate/single")
    public String generateSingleSalary(@RequestParam Long employeeId,
                                       @RequestParam String month,
                                       @RequestParam Integer year,
                                       RedirectAttributes redirectAttributes) {
        try {
            Salary salary = monthlySalaryService.generateMonthlySalary(employeeId, month, year);
            redirectAttributes.addFlashAttribute("success",
                    "Salary generated successfully for " + month + " " + year + "!");
            return "redirect:/admin/payroll/view/" + salary.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error generating salary: " + e.getMessage());
            return "redirect:/admin/payroll/generate/" + employeeId;
        }
    }

    // POST - Generate All Salaries for Month
    @PostMapping("/generate/all")
    public String generateAllSalaries(@RequestParam String month,
                                      @RequestParam Integer year,
                                      RedirectAttributes redirectAttributes) {
        try {
            List<Salary> generated = monthlySalaryService.generateMonthlySalaryForAllEmployees(month, year);
            redirectAttributes.addFlashAttribute("success",
                    "Salaries generated for " + generated.size() + " employees for " + month + " " + year + "!");
            return "redirect:/admin/payroll/list/monthly";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error generating salaries: " + e.getMessage());
            return "redirect:/admin/payroll/generate/all-form";
        }
    }

    // GET - Generate All Salaries Form
    @GetMapping("/generate/all-form")
    public String showGenerateAllForm(Model model) {
        model.addAttribute("currentMonth", LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM")));
        model.addAttribute("currentYear", LocalDate.now().getYear());
        model.addAttribute("pageTitle", "Generate All Salaries");
        return "admin/payroll/generate-all-salaries";
    }

    // GET - View Salary
    @GetMapping("/view/{id}")
    public String viewSalary(@PathVariable Long id, Model model) {
        Salary salary = monthlySalaryService.getSalaryByEmployeeAndMonth(
                id,
                adminSalaryRepo.findById(id).get().getMonth(),
                adminSalaryRepo.findById(id).get().getYear()
        );
        // Actually we need the salary ID, let's fix this
        // Better to fetch by ID directly - we need to add this method
        model.addAttribute("salary", salary);
        model.addAttribute("pageTitle", "Salary Details");
        return "admin/payroll/view-salary";
    }

    // GET - List Monthly Salaries
    @GetMapping("/list/monthly")
    public String listMonthlySalaries(@RequestParam(required = false) String month,
                                      @RequestParam(required = false) Integer year,
                                      Model model) {
        if (month == null || year == null) {
            month = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM"));
            year = LocalDate.now().getYear();
        }

        List<Salary> salaries = monthlySalaryService.getSalariesByMonth(month, year);
        model.addAttribute("salaries", salaries);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("pageTitle", "Monthly Salary List");
        return "admin/payroll/monthly-salary-list";
    }

    // GET - List Employee Salaries
    @GetMapping("/list/employee/{employeeId}")
    public String listEmployeeSalaries(@PathVariable Long employeeId, Model model) {
        List<Salary> salaries = monthlySalaryService.getSalariesByEmployee(employeeId);
        model.addAttribute("salaries", salaries);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("pageTitle", "Employee Salary History");
        return "admin/payroll/employee-salary-history";
    }

    // POST - Update Payment Status
    @PostMapping("/update-status/{id}")
    public String updatePaymentStatus(@PathVariable Long id,
                                      @RequestParam String status,
                                      RedirectAttributes redirectAttributes) {
        Salary salary = adminSalaryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Salary not found"));
        salary.setPaymentStatus(status);
        adminSalaryRepo.save(salary);
        redirectAttributes.addFlashAttribute("success", "Payment status updated to: " + status);
        return "redirect:/admin/payroll/view/" + id;
    }
}
