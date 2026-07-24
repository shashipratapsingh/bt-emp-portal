package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.entity.AttendanceTracking;
import EmployeeManagementSystem.service.AttendanceTrackingService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class AttendanceTrackingController {

    private final AttendanceTrackingService attendanceTrackingService;

    @GetMapping("/attendance-tracking")
    public String attendanceTracking(HttpSession session,
                                     Model model) {

        Long employeeId = (Long) session.getAttribute("employeeId");

        System.out.println("Employee ID: " + employeeId);

        if (employeeId != null) {

            AttendanceTracking attendance =
                    attendanceTrackingService.getTodayAttendance(employeeId);

            model.addAttribute("attendance", attendance);
        }

        return "attendanceTracking";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            Long employeeId =
                    (Long) session.getAttribute("employeeId");

            if (employeeId != null) {
                attendanceTrackingService.markLogout(employeeId);
            }

            session.invalidate();
        }

        Cookie cookie = new Cookie("jwtToken", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return "redirect:/auth/loginPage?logout=true";
    }
}