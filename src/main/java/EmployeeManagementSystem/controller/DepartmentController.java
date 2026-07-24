package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.entity.Department;
import EmployeeManagementSystem.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/add")
    public String showAddDepartmentForm(Model model) {
        model.addAttribute("department", new Department());
        model.addAttribute("departments", departmentService.getAllDepartments()); // FIX: Add this
        model.addAttribute("pageTitle", "Add Department");
        return "admin/employee-management/add-department";
    }

    @PostMapping("/save")
    public String saveDepartment(@Valid @ModelAttribute("department") Department department,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        if (departmentService.existsByDepartmentName(department.getDepartmentName())) {
            bindingResult.rejectValue("departmentName", "error.department", "Department name already exists");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("pageTitle", "Add Department");
            return "admin/employee-management/add-department";
        }

        try {
            departmentService.saveDepartment(department);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Department added successfully!");
            return "redirect:/admin/departments/add";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error adding department: " + e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("pageTitle", "Add Department");
            return "admin/employee-management/add-department";
        }
    }

    @PostMapping("/add-designation")
    public String addDesignation(@RequestParam Long departmentId,
                                 @RequestParam String designationName,
                                 RedirectAttributes redirectAttributes) {
        try {
            Department department = departmentService.getDepartmentById(departmentId);

            List<String> designations = department.getDesignations();
            if (designations == null) {
                designations = new ArrayList<>();
            }

            if (!designations.contains(designationName)) {
                designations.add(designationName);
                department.setDesignations(designations);
                departmentService.updateDepartment(departmentId, department);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Designation '" + designationName + "' added to '" + department.getDepartmentName() + "' successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Designation '" + designationName + "' already exists in this department!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding designation: " + e.getMessage());
        }
        return "redirect:/admin/departments/add";
    }

    @GetMapping("/edit/{id}")
    public String showEditDepartmentForm(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id);
        model.addAttribute("department", department);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("pageTitle", "Edit Department");
        return "admin/employee-management/add-department";
    }

    @PostMapping("/update/{id}")
    public String updateDepartment(@PathVariable Long id,
                                   @Valid @ModelAttribute("department") Department department,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("pageTitle", "Edit Department");
            return "admin/employee-management/add-department";
        }

        try {
            departmentService.updateDepartment(id, department);
            redirectAttributes.addFlashAttribute("successMessage", "Department updated successfully!");
            return "redirect:/admin/departments/add";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating department: " + e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("pageTitle", "Edit Department");
            return "admin/employee-management/add-department";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            departmentService.deleteDepartment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Department deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting department: " + e.getMessage());
        }
        return "redirect:/admin/departments/add";
    }

    @GetMapping("/designations")
    @ResponseBody
    public List<String> getDesignations(@RequestParam String departmentName) {
        return departmentService.getDesignationsByDepartment(departmentName);
    }

    @GetMapping("/add-employee/{departmentId}")
    public String redirectToAddEmployee(@PathVariable Long departmentId, RedirectAttributes redirectAttributes) {
        Department department = departmentService.getDepartmentById(departmentId);
        redirectAttributes.addAttribute("department", department.getDepartmentName());
        return "redirect:/admin/employees/add";
    }

    // Show Manage Designations page
    @GetMapping("/designations/add")
    public String showManageDesignationsPage(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("pageTitle", "Manage Designations");
        return "admin/employee-management/manage-designations";
    }

    // Remove designation from department
    @GetMapping("/remove-designation/{deptId}/{designation}")
    public String removeDesignation(@PathVariable Long deptId,
                                    @PathVariable String designation,
                                    RedirectAttributes redirectAttributes) {
        try {
            Department department = departmentService.getDepartmentById(deptId);
            List<String> designations = department.getDesignations();
            if (designations != null && designations.contains(designation)) {
                designations.remove(designation);
                department.setDesignations(designations);
                departmentService.updateDepartment(deptId, department);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Designation '" + designation + "' removed successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Designation '" + designation + "' not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error removing designation: " + e.getMessage());
        }
        return "redirect:/admin/departments/designations/add";
    }
}