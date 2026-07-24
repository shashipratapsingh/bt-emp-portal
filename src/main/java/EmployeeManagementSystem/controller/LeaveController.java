//package EmployeeManagementSystem.controller;
//
//import EmployeeManagementSystem.entity.LeaveRequest;
//import EmployeeManagementSystem.service.LeaveService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/leave")
//public class LeaveController {
//    private final LeaveService leaveService;
//
//    @GetMapping("/apply")
//    public String showLeaveForm(Model model){
//        model.addAttribute("leaveRequest",new LeaveRequest());
//        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
//        String currentEmpId=authentication.getName();
//        if(authentication == null || !authentication.isAuthenticated() ||
//                "anonymousUser".equals(authentication.getName())){
//            return "redirect:/auth/loginPage";
//        }
//        List<LeaveRequest> myLeaves=leaveService.getLeavesByEmployeeId(currentEmpId);
//        model.addAttribute("myLeaves",myLeaves);
//        return "leave-apply";
//    }
//    @PostMapping("/submit")
//    public String submitLeave(@ModelAttribute("leaveRequest")LeaveRequest leaveRequest){
//
//        leaveService.applyLeave(leaveRequest);
//        return "redirect:/leave/apply?success=true";
//    }
//    @GetMapping("/manage")
//    public String manageLeaves(Model model){
//        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(auth.getAuthorities());
//        List<LeaveRequest> allRequest=leaveService.getAllLeaveRequest();
//        model.addAttribute("allRequest",allRequest);
//        return "leave-manage";
//    }
//    @PostMapping("/status/{id}")
//    public String updateStatus(@PathVariable("id") Long id, @RequestParam("action") String action){
//        leaveService.updateleaveStatus(id,action);
//        return "redirect:/leave/manage";
//    }
//
//}




package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.entity.LeaveRequest;
import EmployeeManagementSystem.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employee/leave")
public class LeaveController {

    private final LeaveService leaveService;

    @GetMapping("/apply")
    public String showLeaveForm(Model model) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getName())) {

            return "redirect:/auth/loginPage";
        }

        String currentEmpId = authentication.getName();

        System.out.println("Logged In Employee ID = " + currentEmpId);

        model.addAttribute("employeeId", currentEmpId);

        model.addAttribute(
                "leaveRequest",
                new LeaveRequest()
        );

        List<LeaveRequest> myLeaves =
                leaveService.getLeavesByEmployeeId(currentEmpId);

        model.addAttribute(
                "myLeaves",
                myLeaves
        );

        return "leave-apply";
    }

    @PostMapping("/submit")
    public String submitLeave(
            @ModelAttribute("leaveRequest")
            LeaveRequest leaveRequest) {

        leaveService.applyLeave(leaveRequest);

        return "redirect:/leave/apply?success=true";
    }

    @GetMapping("/manage")
    public String manageLeaves(Model model) {

        List<LeaveRequest> allRequest =
                leaveService.getAllLeaveRequest();

        model.addAttribute(
                "allRequest",
                allRequest
        );

        return "leave-manage";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam String action) {

        leaveService.updateleaveStatus(id, action);

        return "redirect:/leave/manage";
    }
}