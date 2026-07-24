package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.entity.EmployeeProfile;
import EmployeeManagementSystem.entity.RegisterEmployee;
import EmployeeManagementSystem.jwt.JwtUtil;
import EmployeeManagementSystem.repository.EmployeeProfileRepository;
import EmployeeManagementSystem.service.AttendanceService;
import EmployeeManagementSystem.service.AttendanceTrackingService;
import EmployeeManagementSystem.service.RegisterEmployeeService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.Duration;
import java.time.LocalDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class SidebarGlobalAdvice {
    private final JwtUtil jwtUtil;
    private final RegisterEmployeeService employeeService;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final AttendanceTrackingService attendanceTrackingService;

    @ModelAttribute
    public void addSidebarData(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                    try {
                        String employeeId = jwtUtil.extractUsername(cookie.getValue());
                        RegisterEmployee emp = employeeService.getEmployeeById(employeeId);

                        if (emp != null) {
                            // 1. Name aur ID set karein
                            model.addAttribute("loggedInEmpName", emp.getName());
                            model.addAttribute("loggedInEmpId", emp.getId());
                            model.addAttribute("loggedInEmpDesignation", emp.getDesignation());

                            // 2. Photo set karein (Sahi String ID se)
                            EmployeeProfile empProfile = employeeProfileRepository.findByUserId(employeeId).orElse(null);
                            model.addAttribute("loggedInEmpPhoto", empProfile != null ? empProfile.getPhoto() : null);
                        }
                        // ─── 3. TIMER IMPLEMENTATION (यहाँ जोड़ें) ───
                        long totalSeconds = 0;

                        // अपने AttendanceService के मेथड का नाम चेक कर लें (जैसे getPunchInTimeForToday या जो भी आपने बनाया हो)
                        LocalDateTime punchInTime = attendanceTrackingService.getPunchInTimeForToday(employeeId);

                        if (punchInTime != null) {
                            // Punch-In टाइम और अभी के करंट टाइम का अंतर सेकंड्स में निकालें
                            totalSeconds = Duration.between(punchInTime, LocalDateTime.now()).getSeconds();
                        }

                        // अगर एम्प्लॉई ने आज पंच-इन नहीं किया है या अंतर माइनस में है तो 0 भेजें
                        model.addAttribute("backendTotalSeconds", totalSeconds > 0 ? totalSeconds : 0);
                    } catch (Exception e) {
                        // Token expire ya invalid hone par handle karein
                    }
                    break;
                }
            }
        }

}
}
