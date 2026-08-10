// SalaryStructureController.java
package EmployeeManagementSystem.controller.admin_salary;

import EmployeeManagementSystem.dto.SalaryStructureDTO;
import EmployeeManagementSystem.entity.EmployeeProfile;
import EmployeeManagementSystem.entity.admin_salary.SalaryStructure;
import EmployeeManagementSystem.repository.EmployeeProfileRepository;
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
    private final EmployeeProfileRepository employeeProfileRepository;

    // GET - Add Salary Structure Form
    @GetMapping("/add/{profileId}")
    public String showAddSalaryStructureForm(@PathVariable Long profileId, Model model) {
        EmployeeProfile employeeProfile = employeeProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Employee Profile not found with id: " + profileId));

        // Check if salary structure already exists for this profile
        if (salaryStructureService.existsByEmployeeProfileId(profileId)) {
            SalaryStructure existing = salaryStructureService.getSalaryStructureByEmployeeProfileId(profileId);
            return "redirect:/admin/salary-structure/edit/" + existing.getId();
        }

        SalaryStructureDTO dto = new SalaryStructureDTO();
        dto.setEmployeeProfileId(profileId);

        model.addAttribute("employeeProfile", employeeProfile);
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
            redirectAttributes.addFlashAttribute("success", "Salary structure saved successfully!");
            return "redirect:/admin/salary-structure/view/" + saved.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving salary structure: " + e.getMessage());
            return "redirect:/admin/salary-structure/add/" + dto.getEmployeeProfileId();
        }
    }

    // GET - Edit Salary Structure Form
    @GetMapping("/edit/{id}")
    public String showEditSalaryStructureForm(@PathVariable Long id, Model model) {
        SalaryStructure salaryStructure = salaryStructureService.getSalaryStructure(id);
        EmployeeProfile employeeProfile = salaryStructure.getEmployeeProfile();

        SalaryStructureDTO dto = new SalaryStructureDTO();
        dto.setId(salaryStructure.getId());
        dto.setEmployeeProfileId(employeeProfile.getId());
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

        model.addAttribute("employeeProfile", employeeProfile);
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
            return "redirect:/admin/salary-structure/list";
        }
    }

    // GET - View Salary Structure Details
    @GetMapping("/view/{id}")
    public String viewSalaryStructure(@PathVariable Long id, Model model) {
        SalaryStructure salaryStructure = salaryStructureService.getSalaryStructure(id);
        EmployeeProfile employeeProfile = salaryStructure.getEmployeeProfile();

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
        model.addAttribute("employeeProfile", employeeProfile);
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
                                        @RequestParam(required = false) Long profileId,
                                        RedirectAttributes redirectAttributes) {
        try {
            salaryStructureService.deleteSalaryStructure(id);
            redirectAttributes.addFlashAttribute("success", "Salary structure deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting salary structure: " + e.getMessage());
        }

        if (profileId != null) {
            return "redirect:/admin/employee-profiles/view/" + profileId;
        }
        return "redirect:/admin/salary-structure/list";
    }
}