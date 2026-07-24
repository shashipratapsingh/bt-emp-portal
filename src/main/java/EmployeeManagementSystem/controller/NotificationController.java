package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.service.EmployeeService;
import EmployeeManagementSystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final EmployeeService employeeService;

//    @GetMapping("/notifications")
//    public String notificationPage(
//            Authentication authentication,
//            Model model
//    ) {
//
//        String email = authentication.getName();
//
//        Employee employee = employeeService.findByEmail(email);
//
//        model.addAttribute(
//                "notifications",
//                notificationService.getEmployeeNotification(employee.getId())
//        );
//
//        return "notification";
//    }





    @GetMapping("/notifications")
    public String notificationPage(
            Authentication authentication,
            Model model
    ) {

        System.out.println("Authentication = " + authentication);

        if(authentication == null){
            return "redirect:/login";
        }

        System.out.println("Username = " + authentication.getName());

        String email = authentication.getName();

        Employee employee = employeeService.findByEmail(email);

        model.addAttribute(
                "notifications",
                notificationService.getEmployeeNotification(employee.getId())
        );

        return "notification";
    }
}

