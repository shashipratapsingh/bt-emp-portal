package EmployeeManagementSystem.controller.admin_leave;

import EmployeeManagementSystem.dto.LeaveBalanceAdjustDTO;
import EmployeeManagementSystem.entity.Holiday;
import EmployeeManagementSystem.entity.LeaveRequest;
import EmployeeManagementSystem.entity.admin_leave.LeavePolicy;
import EmployeeManagementSystem.entity.admin_leave.LeaveType;
import EmployeeManagementSystem.service.EmployeeService;
import EmployeeManagementSystem.service.admin_leave.AdminLeaveService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/leave")
public class LeaveAdminController {

    @Autowired
    private AdminLeaveService leaveService;
    @Autowired
    private EmployeeService employeeService;

    // ==================== LEAVE TYPES ====================
    @GetMapping("/types")
    public String listLeaveTypes(Model model) {
        model.addAttribute("leaveTypes", leaveService.findAllLeaveTypes());
        model.addAttribute("leaveType", new LeaveType());
        return "admin/leave-management/leave-types";
    }

    @PostMapping("/types")
    public String saveLeaveType(@Valid @ModelAttribute("leaveType") LeaveType type,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("leaveTypes", leaveService.findAllLeaveTypes());
            return "admin/leave-management/leave-types";
        }
        try {
            leaveService.saveLeaveType(type);
            return "redirect:/admin/leave/types?success";
        } catch (IllegalArgumentException e) {
            result.rejectValue("code", "error.code", e.getMessage());
            model.addAttribute("leaveTypes", leaveService.findAllLeaveTypes());
            return "admin/leave-management/leave-types";
        }
    }

    @GetMapping("/types/delete/{id}")
    public String deleteLeaveType(@PathVariable Long id) {
        try {
            leaveService.deleteLeaveType(id);
        } catch (IllegalStateException e) {
            // handle error via flash attribute
        }
        return "redirect:/admin/leave/types";
    }

    // ==================== LEAVE POLICIES ====================
    @GetMapping("/policies")
    public String listPolicies(Model model) {
        model.addAttribute("policies", leaveService.findAllPolicies());
        model.addAttribute("policy", new LeavePolicy());
        model.addAttribute("leaveTypes", leaveService.findAllLeaveTypes());
        return "admin/leave-management/leave-policies";
    }

    @PostMapping("/policies")
    public String savePolicy(@Valid @ModelAttribute("policy") LeavePolicy policy,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("policies", leaveService.findAllPolicies());
            model.addAttribute("leaveTypes", leaveService.findAllLeaveTypes());
            return "admin/leave-management/leave-policies";
        }
        try {
            leaveService.savePolicy(policy);
            return "redirect:/admin/leave/policies?success";
        } catch (IllegalArgumentException e) {
            result.rejectValue("leaveType", "error.leaveType", e.getMessage());
            model.addAttribute("policies", leaveService.findAllPolicies());
            model.addAttribute("leaveTypes", leaveService.findAllLeaveTypes());
            return "admin/leave-management/leave-policies";
        }
    }

    // ==================== HOLIDAYS ====================
    @GetMapping("/holidays")
    public String listHolidays(Model model) {
        model.addAttribute("holidays", leaveService.findAllHolidays());
        model.addAttribute("holiday", new Holiday());
        return "admin/leave-management/holidays";
    }

    @PostMapping("/holidays")
    public String saveHoliday(@Valid @ModelAttribute("holiday") Holiday holiday,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("holidays", leaveService.findAllHolidays());
            return "admin/leave-management/holidays";
        }
        try {
            leaveService.saveHoliday(holiday);
            return "redirect:/admin/leave/holidays?success";
        } catch (IllegalArgumentException e) {
            result.rejectValue("holidayDate", "error.date", e.getMessage());
            model.addAttribute("holidays", leaveService.findAllHolidays());
            return "admin/leave-management/holidays";
        }
    }

    @GetMapping("/holidays/delete/{id}")
    public String deleteHoliday(@PathVariable Long id) {
        leaveService.deleteHoliday(id);
        return "redirect:/admin/leave/holidays";
    }

    // ==================== LEAVE REQUESTS (APPROVAL) ====================
    @GetMapping("/requests")
    public String leaveRequests(@RequestParam(defaultValue = "PENDING") String status,
                                Model model) {
        List<LeaveRequest> requests = leaveService.findRequestsByStatus(status);
        System.out.println("Number of leave requests for " + status + ": " + requests.size());
        // You can also print each request to see their details
        requests.forEach(req -> System.out.println(req.getEmployeeName() + " - " + req.getStatus()));
        model.addAttribute("requests", leaveService.findRequestsByStatus(status));
        model.addAttribute("currentStatus", status);
        return "admin/leave-management/leave-requests";
    }

    @PostMapping("/requests/{id}/approve")
    public String approveRequest(@PathVariable Long id,
                                 @RequestParam(required = false) String comment,
                                 RedirectAttributes ra) {
        try {
            leaveService.approveLeave(id, comment);
            ra.addFlashAttribute("success", "Leave approved successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/leave/requests?status=PENDING";
    }

    @PostMapping("/requests/{id}/reject")
    public String rejectRequest(@PathVariable Long id,
                                @RequestParam(required = false) String comment,
                                RedirectAttributes ra) {
        try {
            leaveService.rejectLeave(id, comment);
            ra.addFlashAttribute("success", "Leave rejected");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/leave/requests?status=PENDING";
    }

    // ==================== MANUAL BALANCE ADJUSTMENT ====================
    @GetMapping("/balance/adjust")
    public String showAdjustBalanceForm(Model model) {
        model.addAttribute("adjustDTO", new LeaveBalanceAdjustDTO());
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("leaveTypes", leaveService.findAllLeaveTypes());
        return "admin/leave-management/adjust-balance";
    }

    @PostMapping("/balance/adjust")
    public String adjustBalance(@Valid @ModelAttribute("adjustDTO") LeaveBalanceAdjustDTO dto,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.findAll());
            model.addAttribute("leaveTypes", leaveService.findAllLeaveTypes());
            return "admin/leave-management/adjust-balance";
        }
        try {
            leaveService.adjustLeaveBalance(dto);
            return "redirect:/admin/leave/balance/adjust?success";
        } catch (IllegalArgumentException e) {
            result.rejectValue("delta", "error.delta", e.getMessage());
            model.addAttribute("employees", employeeService.findAll());
            model.addAttribute("leaveTypes", leaveService.findAllLeaveTypes());
            return "admin/leave-management/adjust-balance";
        }
    }
}