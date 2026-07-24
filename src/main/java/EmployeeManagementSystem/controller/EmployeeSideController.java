package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.dto.AnniversaryDTO;
import EmployeeManagementSystem.dto.BirthdayDTO;
import EmployeeManagementSystem.dto.CelebrationDto;
import EmployeeManagementSystem.entity.EmployeeProfile;
import EmployeeManagementSystem.entity.Policy;
import EmployeeManagementSystem.entity.RegisterEmployee;
import EmployeeManagementSystem.entity.WfhRequest;
import EmployeeManagementSystem.enums.LeaveStatus;
import EmployeeManagementSystem.jwt.JwtUtil;
import EmployeeManagementSystem.repository.EmployeeProfileRepository;
import EmployeeManagementSystem.repository.LeaveRepository;
import EmployeeManagementSystem.repository.WfhRequestRepository;
import EmployeeManagementSystem.service.*;
import EmployeeManagementSystem.service.EmployeeService;
//import EmployeeManagementSystem.service.PolicyService;
import EmployeeManagementSystem.service.RegisterEmployeeService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeSideController {
    private final JwtUtil jwtUtil;
    private final RegisterEmployeeService service;
    private final EmployeeService employeeService;
    private final PolicyService policyService;
    private final AttendanceService attendanceService;
    private final ProjectOffService projectOffService;
    private final WfhService wfhService;
    private final LeaveService leaveService;
    private final WfhRequestRepository wfhRequestRepository;
    private final LeaveRepository leaveRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final EmployeeProfileService employeeProfileService;
//    private final PolicyService policyService;
    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        boolean isLoggedIn = false;
        String employeeName = "DASHBOARD";
        Long loggedInEmpId = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                    isLoggedIn = true;
                    String employeeId = jwtUtil.extractUsername(cookie.getValue());
                    RegisterEmployee emp = service.getEmployeeById(employeeId);

                    if (emp != null) {
                        employeeName = emp.getName();
                        loggedInEmpId = Long.valueOf(emp.getId());
                    }
                    break;
                }
            }
        }

        List<BirthdayDTO> birthdayEmployee = employeeService.getUpcomingBirthdays();
        List<AnniversaryDTO> upcomingAnniversaries = employeeService.getUpcomingAnniversaries();
        List<CelebrationDto> celebrations = employeeProfileService.getTodayCelebrations();

        model.addAttribute("celebrations",celebrations);

        model.addAttribute("isUserLoggedIn", isLoggedIn);
        model.addAttribute("loggedInEmpName", employeeName);
        model.addAttribute("birthdayList", birthdayEmployee);
        model.addAttribute("anniversaryList", upcomingAnniversaries);
        model.addAttribute("attendance", attendanceService.getTodayAttendance());
        model.addAttribute("wfhList", attendanceService.getTodayWFHEmployees());
        model.addAttribute("wfhList",wfhService.getWFHEmployees());
        model.addAttribute("leaveList",leaveService.getAllLeaveRequest());
        model.addAttribute("totalWFH",wfhRequestRepository.countByStatus("APPROVED"));
        model.addAttribute("totalLeaves",leaveRepository.countByStatus(LeaveStatus.APPROVED));


        model.addAttribute("projectOffLogs", projectOffService.getTodayProjectOffLogs());

        model.addAttribute("loggedInEmpId", loggedInEmpId);
        //model.addAttribute("loggedInEmpId", loggedInEmpId);

        //FIX 1: Token se nikli hui asli employeeId (String) ka use karein, na ki numeric auto-incremented ID ka
        String realEmployeeId = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                    realEmployeeId = jwtUtil.extractUsername(cookie.getValue());
                    break;
                }
            }
        }

        //FIX 2: Sahi ID se profile fetch karein aur refresh hone par bhi photo permanent dikhegi
        if (realEmployeeId != null) {
            EmployeeProfile empProfile = employeeProfileRepository.findByUserId(realEmployeeId).orElse(null);
            model.addAttribute("loggedInEmpPhoto", empProfile != null ? empProfile.getPhoto() : null);
        } else {
            model.addAttribute("loggedInEmpPhoto", null);
        }

        return "employeeside-dashboard";
    }

    @GetMapping("/profile")
    public String profile(){
        return "employee/profile";
    }

    @GetMapping("/wfh/apply")
    public String showWfhForm(Model model) {
        model.addAttribute("currentPage", "wfh");
        model.addAttribute("wfhRequest", new WfhRequest());
        return "wfh-apply-page";
    }
    @PostMapping("/wfh/save")
    public String saveWfhRequest(@ModelAttribute("wfhRequest") WfhRequest request, @AuthenticationPrincipal UserDetails currentUser){
        if (currentUser!=null){
            request.setEmployeeId(currentUser.getUsername());
            request.setEmployeeName(currentUser.getUsername());
        }
        wfhService.saveRequest(request);
        return "redirect:/employee/wfh/apply?success=true";
    }

    @GetMapping("/policy")
    public String viewPolicy(Model model){
        List<Policy> policies=policyService.getAllPolicy();
        model.addAttribute("policies",policies);
        return "policies";
    }

//    @GetMapping("/policy")
//    public String viewPolicy(Model model){
//        List<Policy> policies=policyService.getAllPolicy();
//        model.addAttribute("policies",policies);
//        return "policies";
//    }

}
