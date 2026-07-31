package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.dto.LoginRequest;
import EmployeeManagementSystem.entity.AttendanceTracking;
import EmployeeManagementSystem.entity.LoginLocation;
import EmployeeManagementSystem.entity.RegisterEmployee;
import EmployeeManagementSystem.entity.UserSession;
import EmployeeManagementSystem.jwt.JwtUtil;
import EmployeeManagementSystem.repository.AttendanceTrackingRepository;
import EmployeeManagementSystem.repository.UserSessionRepository;
import EmployeeManagementSystem.service.AttendanceService;
import EmployeeManagementSystem.service.LoginLocationService;
import EmployeeManagementSystem.service.RegisterEmployeeService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import EmployeeManagementSystem.service.GeofenceService;

import EmployeeManagementSystem.entity.LoginLocation;
import java.time.LocalDateTime;
import EmployeeManagementSystem.entity.LoginLocation;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {
    private final RegisterEmployeeService service;
    private final AttendanceTrackingRepository attendanceTrackingRepository;
    private  final JwtUtil jwtUtil;
    private final UserSessionRepository userSessionRepository;
    private final GeofenceService geofenceService;
    private final LoginLocationService loginLocationService;

    @GetMapping("/employeeRegisterForm")
    public String openForm(Model model){
        model.addAttribute("registerEmployee",new RegisterEmployee());
        return "register";
    }
    @PostMapping("/register")
    public String createUser(@ModelAttribute RegisterEmployee registerEmployee,Model model)
    {
         String generatedPassword=service.registerEmployee(registerEmployee);
        model.addAttribute("message", "Employee Registered Successfully!");
        model.addAttribute("generatedId", registerEmployee.getUserId());
        model.addAttribute("generatedPassword", generatedPassword);

        return "register-success";
    }
    @GetMapping("/loginPage")
    public String openLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }
//    @PostMapping("/login")
//    public String login(@ModelAttribute("loginRequest") LoginRequest request,
//                        HttpServletResponse response,Model model) {
//        try {
//            String employeeId=request.getUserId();
//
//            String token = service.login(request);
//
//            Cookie jwtCookie = new Cookie("jwtToken", token);
//            jwtCookie.setHttpOnly(true);
//            jwtCookie.setPath("/");
//            jwtCookie.setMaxAge(60 * 60);
//
//            response.addCookie(jwtCookie);
//            UserSession session = new UserSession();
//
//            session.setEmployeeId(employeeId);
//            session.setJwtToken(token);
//            session.setIsActive(true);
//            session.setCreatedAt(LocalDateTime.now());
//            session.setExpiresAt(LocalDateTime.now().plusHours(1));
//
//            userSessionRepository.save(session);
//            String role = jwtUtil.extractRole(token);
//
//            System.out.println("ROLE = " + role);
//
////            HttpSession session = (HttpSession) request.getSession();
//
////            session.setAttribute("employeeId", employeeId);
////            session.setAttribute("employeeName", employeeName);
//            Optional<AttendanceTracking> existing =
//                    attendanceTrackingRepository.findByEmployeeIdAndDate(
//                            employeeId,
//                            LocalDate.now());
//            if (existing.isEmpty()) {
//
//                AttendanceTracking attendance = new AttendanceTracking();
//                attendance.setEmployeeId(employeeId);
//                //attendance.setEmployeeName(registerEmployee.getName());
//                attendance.setDate(LocalDate.now());
//                attendance.setLoginTime(LocalDateTime.now());
//                attendance.setStatus("Present");
//
//                attendanceTrackingRepository.save(attendance);
//            }
//
//            System.out.println("Attendance Saved Successfully");
//
//            if ("ROLE_EMPLOYEE".equalsIgnoreCase(role)){
//                return "redirect:/employee/dashboard";
//            }
//            if ("ROLE_MANAGER".equalsIgnoreCase(role)){
//                return "redirect:/manager/dashboard";
//            }
//            if ("ROLE_ADMIN".equalsIgnoreCase(role)){
//                return "redirect:/admin/dashboard";
//            }
//            if ("ROLE_HR".equalsIgnoreCase(role)){
//                return "redirect:/hr/dashboard";
//            }
//            return "redirect:/login";
//
//        } catch (Exception e) {
//
//            model.addAttribute("error",
//                    "Invalid Credentials: " + e.getMessage());
//            model.addAttribute("loginRequest",new LoginRequest());
//
//            return "login";
//        }
//    }






@PostMapping("/login")
public String login(@ModelAttribute("loginRequest") LoginRequest request,
                    HttpServletResponse response,
                    Model model) {

    try {

        if ("WFO".equalsIgnoreCase(request.getWorkMode())) {

            if (request.getLatitude() == null || request.getLongitude() == null) {
                model.addAttribute("error",
                        "Please allow location access for Work From Office login.");
                model.addAttribute("loginRequest", request);
                return "login";
            }

            boolean insideOffice = geofenceService.isInsideOffice(
                    request.getLatitude(),
                    request.getLongitude());

            if (!insideOffice) {
                model.addAttribute("error",
                        "You are outside the office area. Work From Office login is not allowed.");
                model.addAttribute("loginRequest", request);
                return "login";
            }

            // Optional: Calculate distance from office Calculate employee distance from office
            double distance = geofenceService.getDistance(
                    request.getLatitude(),
                    request.getLongitude());

            System.out.println("Employee Distance From Office : " + distance + " meters");


            LoginLocation loginLocation = new LoginLocation();

            loginLocation.setEmployeeId(request.getUserId());
            loginLocation.setLatitude(request.getLatitude());
            loginLocation.setLongitude(request.getLongitude());
            loginLocation.setDistanceFromOffice(distance);

            loginLocation.setWorkMode(request.getWorkMode());      // WFO
            loginLocation.setLoginStatus("SUCCESS");               // or LOGIN_SUCCESS
            loginLocation.setLoginDate(LocalDate.now());           // <-- Missing
            loginLocation.setLoginTime(LocalDateTime.now());

            loginLocationService.save(loginLocation);        }

        String employeeId = request.getUserId();
        String token = service.login(request);

        Cookie jwtCookie = new Cookie("jwtToken", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60);

        response.addCookie(jwtCookie);


        UserSession session = new UserSession();
        session.setEmployeeId(employeeId);
        session.setJwtToken(token);
        session.setIsActive(true);
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusHours(1));

        userSessionRepository.save(session);


        Optional<AttendanceTracking> existing =
                attendanceTrackingRepository.findByEmployeeIdAndDate(
                        employeeId,
                        LocalDate.now());

        if (existing.isEmpty()) {

            AttendanceTracking attendance = new AttendanceTracking();
            attendance.setEmployeeId(employeeId);
            attendance.setDate(LocalDate.now());
            attendance.setLoginTime(LocalDateTime.now());
            attendance.setStatus("Present");

            attendanceTrackingRepository.save(attendance);
        }

        System.out.println("Attendance Saved Successfully");

        String role = jwtUtil.extractRole(token);

        if ("ROLE_EMPLOYEE".equalsIgnoreCase(role)) {
            return "redirect:/employee/dashboard";
        }

        if ("ROLE_MANAGER".equalsIgnoreCase(role)) {
            return "redirect:/manager/dashboard";
        }

        if ("ROLE_ADMIN".equalsIgnoreCase(role)) {
            return "redirect:/admin/dashboard";
        }

        if ("ROLE_HR".equalsIgnoreCase(role)) {
            return "redirect:/hr/dashboard";
        }

        model.addAttribute("error", "Invalid user role.");
        model.addAttribute("loginRequest", request);
        return "login";

    } catch (Exception e) {

        e.printStackTrace();

        model.addAttribute("error", e.getMessage());
        model.addAttribute("loginRequest", request);

        return "login";
    }
}




    @PostMapping("/check-location")
    @ResponseBody
    public Map<String, Object> checkLocation(@RequestBody LoginRequest request) {

        Map<String, Object> response = new HashMap<>();

        try {

            // Validate GPS
            if (request.getLatitude() == null || request.getLongitude() == null) {

                response.put("inside", false);
                response.put("distance", 0);
                response.put("message", "Location not received.");

                return response;
            }


            double distance = geofenceService.getDistance(
                    request.getLatitude(),
                    request.getLongitude());

            boolean inside = geofenceService.isInsideOffice(
                    request.getLatitude(),
                    request.getLongitude());

            response.put("inside", inside);
            response.put("distance", Math.round(distance));
            response.put("radius", geofenceService.getOffice().getAllowedRadius());

            if (inside) {

                response.put("message",
                        "You are inside the office range. You can login.");

            } else {

                response.put("message",
                        "You are outside the office range.");

            }

        } catch (Exception e) {

            response.put("inside", false);
            response.put("distance", 0);
            response.put("message", e.getMessage());
        }

        return response;
    }




@GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        try {
            service.sendOtpProcessing(email);
            model.addAttribute("email", email);
            return "reset-password";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "forgot-password";
        }
    }
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("email") String email,
                                       @RequestParam("otp") String otp,
                                       @RequestParam("newPassword") String newPassword,
                                       Model model) {
        try {
            service.verifyOtpAndChangePassword(email, otp, newPassword);
            model.addAttribute("message", "Password changed successfully! Please login.");
            model.addAttribute("loginRequest", new LoginRequest());
            return "login";
        } catch (Exception e) {
            model.addAttribute("email", email);
            model.addAttribute("error", e.getMessage());

            return "reset-password";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String employeeId = authentication.getName();

        AttendanceTracking attendance =
                attendanceTrackingRepository
                        .findTopByEmployeeIdAndDateOrderByLoginTimeDesc(
                                employeeId,
                                LocalDate.now());

        attendance.setLogoutTime(LocalDateTime.now());

        Duration duration =
                Duration.between(attendance.getLoginTime(),
                        attendance.getLogoutTime());

        attendance.setWorkingHours(duration.toMinutes() / 60.0);

        attendanceTrackingRepository.save(attendance);

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    cookie.setHttpOnly(true);

                    response.addCookie(cookie);
                    break;
                }
            }
        }

        return "redirect:/auth/loginPage?logout=true";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
