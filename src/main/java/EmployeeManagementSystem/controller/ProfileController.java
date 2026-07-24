package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.entity.EmployeeProfile;
import EmployeeManagementSystem.service.EmployeeProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/employee/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final EmployeeProfileService service;

    @GetMapping("/view")
    public String viewProfile(Model model){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        String currentEmplId=auth. getName();
        EmployeeProfile profile = service.getProfileByUserId(currentEmplId);
        model.addAttribute("profile",profile);
        return "profile-view";
    }
    @PostMapping("/save")
    public String saveProfile(@ModelAttribute("profile") EmployeeProfile profile){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        String currentEmpId=auth.getName();
        service.saveOrUpdateProfile(profile,currentEmpId);
        return "redirect:/profile/view?success=true";
    }

    @PostMapping("/upload-photo")
    public ResponseEntity<?> uploadPhoto(@RequestParam("photo") MultipartFile file) {

        try {

            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            String employeeId = authentication.getName();

            String photo = service.uploadPhoto(file, employeeId);

            return ResponseEntity.ok(photo);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());

        }

    }
}
