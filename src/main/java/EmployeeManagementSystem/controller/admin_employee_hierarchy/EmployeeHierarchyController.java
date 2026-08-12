//package EmployeeManagementSystem.controller.admin_employee_hierarchy;
//
//import EmployeeManagementSystem.dto.EmployeeHierarchyDTO;
//import EmployeeManagementSystem.service.EmployeeHierarchyService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class EmployeeHierarchyController {
//
//    private final EmployeeHierarchyService hierarchyService;
//
//    @GetMapping("/employee/hierarchy")
//    public String employeeHierarchy(Model model) {
//
//        List<EmployeeHierarchyDTO> hierarchy =
//                hierarchyService.getEmployeeHierarchy();
//
//        model.addAttribute("hierarchy", hierarchy);
//
//        return "employee-hierarchy";
//    }
//}