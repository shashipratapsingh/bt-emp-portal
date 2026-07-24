package EmployeeManagementSystem.controller;//package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.Salary;
import EmployeeManagementSystem.repository.EmployeeRepository;
import EmployeeManagementSystem.service.SalaryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("employee/salary")
public class SalaryController {

    private final SalaryService salaryService;

    private final EmployeeRepository employeeRepository;

    public SalaryController(SalaryService salaryService, EmployeeRepository employeeRepository) {
        this.salaryService = salaryService;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("salaryList",
                salaryService.getAllSalaries());

        model.addAttribute("totalEmployees",
                salaryService.getTotalEmployees());

        model.addAttribute("paidCount",
                salaryService.getPaidEmployeesCount());

        model.addAttribute("pendingCount",
                salaryService.getPendingEmployeesCount());

        model.addAttribute("totalPayroll",
                salaryService.getTotalPayrollCost());

        model.addAttribute("paidSalary",
                salaryService.getTotalPaidSalary());

        model.addAttribute("pendingSalary",
                salaryService.getTotalPendingSalary());

        return "salary-dashboard";
    }

    @GetMapping("/add")
    public String addSalaryForm(Model model) {

        model.addAttribute("salary", new Salary());

        return "salary-form";
    }

    @PostMapping("/save")
    public String saveSalary(@ModelAttribute Salary salary) {

        salaryService.saveSalary(salary);

        return "redirect:/salary/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String editSalary(@PathVariable Long id,
                             Model model) {

        model.addAttribute("salary",
                salaryService.getSalaryById(id));

        return "salary-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteSalary(@PathVariable Long id) {

        salaryService.deleteSalary(id);

        return "redirect:/salary/dashboard";
    }

//    @GetMapping("/slip/{id}")
//    public String salarySlip(@PathVariable Long id,
//                             Model model) {
//
//        model.addAttribute("salarySlip",
//                salaryService.getSalarySlipById(id));
//
//        return "salary-slip";
//    }

    @GetMapping("/slip/{id}")
    public String salarySlip(@PathVariable Long id,
                             Model model) {

        Salary salary = salaryService.getSalaryById(id);

        System.out.println("Salary Found : " + salary);

        model.addAttribute("salary", salary);

        return "salary-slip";
    }


    @GetMapping("/salary-list")
    public String salaryList(Model model) {

        List<Salary> salaries = salaryService.getAllSalaries();

        model.addAttribute("salaries", salaries);

        return "salary-list";
    }



}


//
//
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/admin/salary")
//public class SalaryController {
//
//    @GetMapping("/salary-dashboard")  // Match the URL you're trying to access
//    public String salaryDashboard(Model model) {
//        // ... your code
//        return "admin/salary/salary-dashboard";
//    }
//}