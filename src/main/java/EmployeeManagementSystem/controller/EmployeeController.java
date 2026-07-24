package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.Salary;
import EmployeeManagementSystem.service.DepartmentService;
import EmployeeManagementSystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public String getAllEmployees(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            Model model) {

        String cleanKeyword = (keyword == null) ? "" : keyword.trim();

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy == null || sortBy.isBlank() ? "id" : sortBy).ascending()
        );

        Page<Employee> employeePage;

        if (cleanKeyword.isEmpty()) {
            employeePage = employeeService.getAllEmployees(page, size, sortBy);
        } else {
            employeePage = employeeService.searchEmployee(cleanKeyword, pageable);
        }

        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("size", size);
        model.addAttribute("keyword", cleanKeyword);

        return "employee-list";
    }


    @GetMapping("/add")
    public String showAddForm(Model model) {

        Employee employee = new Employee();

        model.addAttribute("employee", employee);
        model.addAttribute("departments", departmentService.getAllDepartments());

        return "employee-form";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee,
                               @RequestParam("imageFile") MultipartFile file) {

        try {
            if (file != null && !file.isEmpty()) {

                String uploadDirPath = System.getProperty("user.dir") + "/uploads/";
                File uploadDir = new File(uploadDirPath);

                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String fileName = System.currentTimeMillis()
                        + "_" + file.getOriginalFilename();

                File destination = new File(uploadDir, fileName);
                file.transferTo(destination);

                employee.setImageName(fileName);
            }

            if (employee.getId() != null) {
                employeeService.updateEmployee(employee.getId(), employee);
            } else {
                employeeService.saveEmployee(employee);
            }

            return "redirect:/employees";

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/edit/{id}")
    public String editEmployee(@PathVariable Long id, Model model) {

        Employee employee = employeeService.getEmployeeById(id);


        if (employee.getSalaryDetails() == null) {
            employee.setSalaryDetails(new Salary());
        }

        model.addAttribute("employee", employee);
        model.addAttribute("departments", departmentService.getAllDepartments());

        return "employee-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }
}