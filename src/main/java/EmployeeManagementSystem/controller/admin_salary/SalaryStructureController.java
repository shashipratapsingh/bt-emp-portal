package EmployeeManagementSystem.controller.admin_salary;


import EmployeeManagementSystem.dto.SalaryStructureDTO;
import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.admin_salary.SalaryStructure;
import EmployeeManagementSystem.repository.EmployeeRepository;
import EmployeeManagementSystem.service.admin_salary.SalaryStructureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/salary-structure")
@RequiredArgsConstructor
public class SalaryStructureController {

    private final SalaryStructureService salaryStructureService;
    private final EmployeeRepository employeeRepository;

    // GET - Add Salary Structure Form
    @GetMapping("/add/{employeeId}")
    public String showAddSalaryStructureForm(@PathVariable Long employeeId, Model model) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

        // Check if salary structure already exists
        if (salaryStructureService.existsByEmployeeId(employeeId)) {
            return "redirect:/admin/salary-structure/edit/" +
                    salaryStructureService.getSalaryStructureByEmployeeId(employeeId).getId();
        }

        SalaryStructureDTO dto = new SalaryStructureDTO();
        dto.setEmployeeId(employeeId);

        model.addAttribute("employee", employee);
        model.addAttribute("salaryStructureDTO", dto);
        model.addAttribute("pageTitle", "Add Salary Structure");

        return "admin/salary-structure/add-salary-structure";
    }

    // POST - Save Salary Structure
    @PostMapping("/save")
    public String saveSalaryStructure(@ModelAttribute("salaryStructureDTO") SalaryStructureDTO dto,
                                      RedirectAttributes redirectAttributes) {
        try {
            SalaryStructure saved = salaryStructureService.saveSalaryStructure(dto);
            redirectAttributes.addFlashAttribute("success", "Salary structure saved successfully for employee!");
            return "redirect:/admin/salary-structure/view/" + saved.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving salary structure: " + e.getMessage());
            return "redirect:/admin/salary-structure/add/" + dto.getEmployeeId();
        }
    }

    // GET - Edit Salary Structure Form
    @GetMapping("/edit/{id}")
    public String showEditSalaryStructureForm(@PathVariable Long id, Model model) {
        SalaryStructure salaryStructure = salaryStructureService.getSalaryStructure(id);
        Employee employee = salaryStructure.getEmployee();

        SalaryStructureDTO dto = new SalaryStructureDTO();
        dto.setId(salaryStructure.getId());
        dto.setEmployeeId(employee.getId());
        dto.setBasicSalary(salaryStructure.getBasicSalary());
        dto.setHra(salaryStructure.getHra());
        dto.setConveyance(salaryStructure.getConveyance());
        dto.setMedicalAllowance(salaryStructure.getMedicalAllowance());
        dto.setSpecialAllowance(salaryStructure.getSpecialAllowance());
        dto.setOtherAllowance(salaryStructure.getOtherAllowance());
        dto.setPf(salaryStructure.getPf());
        dto.setEsi(salaryStructure.getEsi());
        dto.setProfessionalTax(salaryStructure.getProfessionalTax());
        dto.setTds(salaryStructure.getTds());
        dto.setLoanDeduction(salaryStructure.getLoanDeduction());
        dto.setEffectiveFrom(salaryStructure.getEffectiveFrom());

        model.addAttribute("employee", employee);
        model.addAttribute("salaryStructureDTO", dto);
        model.addAttribute("pageTitle", "Edit Salary Structure");

        return "admin/salary-structure/edit-salary-structure";
    }

    // POST - Update Salary Structure
    @PostMapping("/update")
    public String updateSalaryStructure(@ModelAttribute("salaryStructureDTO") SalaryStructureDTO dto,
                                        RedirectAttributes redirectAttributes) {
        try {
            SalaryStructure updated = salaryStructureService.updateSalaryStructure(dto);
            redirectAttributes.addFlashAttribute("success", "Salary structure updated successfully!");
            return "redirect:/admin/salary-structure/view/" + updated.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating salary structure: " + e.getMessage());
//            return "redirect:/admin/salary-structure/edit/" + dto.getEmployeeId();
              return "redirect:/admin/salary-structure/list";
        }
    }

    // GET - View Salary Structure Details
    @GetMapping("/view/{id}")
    public String viewSalaryStructure(@PathVariable Long id, Model model) {
        SalaryStructure salaryStructure = salaryStructureService.getSalaryStructure(id);
        Employee employee = salaryStructure.getEmployee();

        // Calculate totals
        BigDecimal totalEarnings = salaryStructure.getBasicSalary()
                .add(salaryStructure.getHra())
                .add(salaryStructure.getConveyance())
                .add(salaryStructure.getMedicalAllowance())
                .add(salaryStructure.getSpecialAllowance())
                .add(salaryStructure.getOtherAllowance());

        BigDecimal totalDeductions = salaryStructure.getPf()
                .add(salaryStructure.getEsi())
                .add(salaryStructure.getProfessionalTax())
                .add(salaryStructure.getTds())
                .add(salaryStructure.getLoanDeduction());

        BigDecimal netSalary = totalEarnings.subtract(totalDeductions);

        model.addAttribute("salaryStructure", salaryStructure);
        model.addAttribute("employee", employee);
        model.addAttribute("totalEarnings", totalEarnings);
        model.addAttribute("totalDeductions", totalDeductions);
        model.addAttribute("netSalary", netSalary);
        model.addAttribute("pageTitle", "View Salary Structure");

        return "admin/salary-structure/view-salary-structure";
    }

    // GET - List All Salary Structures
    @GetMapping("/list")
    public String listSalaryStructures(Model model) {
        List<SalaryStructure> salaryStructures = salaryStructureService.getAllSalaryStructures();
        model.addAttribute("salaryStructures", salaryStructures);
        model.addAttribute("pageTitle", "Salary Structures List");
        return "admin/salary-structure/salary-structure-list";
    }

    // GET - Delete Salary Structure
    @GetMapping("/delete/{id}")
    public String deleteSalaryStructure(@PathVariable Long id,
                                        @RequestParam(required = false) Long employeeId,
                                        RedirectAttributes redirectAttributes) {
        try {
            salaryStructureService.deleteSalaryStructure(id);
            redirectAttributes.addFlashAttribute("success", "Salary structure deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting salary structure: " + e.getMessage());
        }

        if (employeeId != null) {
            return "redirect:/admin/employees/view/" + employeeId;
        }
        return "redirect:/admin/salary-structure/list";
    }
}
