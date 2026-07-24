package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.entity.EmployeeProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    @GetMapping("/dashboard")
    public String  dashboard(){
        return "manager-dashboard";
    }
    @GetMapping("/profile")
    public String profile(Model model){
        model.addAttribute("profile",new EmployeeProfile());
        return "manager-profile";
    }
}
