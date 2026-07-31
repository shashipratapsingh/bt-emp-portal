package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.entity.Project;
import EmployeeManagementSystem.entity.Timesheet;
import EmployeeManagementSystem.service.ProjectService;
import EmployeeManagementSystem.service.TimesheetService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//@Controller
//@RequestMapping("/employee/timesheet")
//@RequiredArgsConstructor
//public class TimesheetController {
//    private final TimesheetService service;
//    @Getter
//    private final ProjectService projectService;
//    @GetMapping("/log")
//    public String OpenTimesheet(Model model){
//        Timesheet sheet = new Timesheet();
//        sheet.setDate(LocalDate.now());
//        model.addAttribute("timesheet", sheet);
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String currentEmpId = auth.getName();
//
//        List<Timesheet> myLogs = service.getTimesheetsByEmployee(currentEmpId);
//        model.addAttribute("myLogs", myLogs);
//        return "timesheet-log";
//    }
//    @PostMapping("/submit")
//    public String saveTimeSheet(@ModelAttribute("timesheet")Timesheet timesheet){
//        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
//        timesheet.setEmployeeName(auth.getName());
//        timesheet.setEmployeeId(auth.getName());
//        service.saveTimesheet(timesheet);
//        return "redirect:/timesheet/log";
//    }
//    @GetMapping("/manage")
//    public String manageTimesheet(Model model){
//        List<Timesheet> allLogs=service.getAllTimesheet();
//        model.addAttribute("allLogs",allLogs);
//        return "timesheet-manage";
//    }
//    @PostMapping("/status/{id}")
//    public String updateStatus(@PathVariable("id") Long id,@RequestParam("action") String action){
//        service.updateTimesheetStatus(id,action);
//        return "redirect:/timesheet/manage";
//    }
//
//}




//@Controller
//@RequestMapping("/employee/timesheet")
//@RequiredArgsConstructor
//public class TimesheetController {
//
//    private final TimesheetService service;
//    private final ProjectService projectService;
//
//    @GetMapping("/log")
//    public String openTimesheet(Model model) {
//
//        // Create Timesheet Object
//        Timesheet sheet = new Timesheet();
//        sheet.setDate(LocalDate.now());
//        model.addAttribute("timesheet", sheet);
//
//        // Logged-in Employee
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String currentEmpId = auth.getName();
//
//        // Employee Timesheets
//        List<Timesheet> myLogs = service.getTimesheetsByEmployee(currentEmpId);
//        model.addAttribute("myLogs", myLogs);
//
//        // Fetch Projects from Database
//        List<Project> projects = projectService.getAllProjects();
//
//        System.out.println("================================");
//        System.out.println("Projects Found : " + projects.size());
//
//        for (Project p : projects) {
//            System.out.println("ID : " + p.getId());
//            System.out.println("Project : " + p.getProjectName());
//        }
//
//        model.addAttribute("projects", projects);
//
//        return "timesheet-log";
//    }
//
//    @PostMapping("/submit")
//    public String saveTimeSheet(@ModelAttribute("timesheet") Timesheet timesheet) {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        timesheet.setEmployeeId(auth.getName());
//        timesheet.setEmployeeName(auth.getName());
//
//        service.saveTimesheet(timesheet);
//
//        return "redirect:/employee/timesheet/log?success";
//    }
//
//    @GetMapping("/manage")
//    public String manageTimesheet(Model model) {
//
//        List<Timesheet> allLogs = service.getAllTimesheet();
//        model.addAttribute("allLogs", allLogs);
//
//        return "timesheet-manage";
//    }
//
//    @PostMapping("/status/{id}")
//    public String updateStatus(@PathVariable Long id,
//                               @RequestParam String action) {
//
//        service.updateTimesheetStatus(id, action);
//
//        return "redirect:/employee/timesheet/manage";
//    }
//}

















//package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.entity.Project;
import EmployeeManagementSystem.entity.Timesheet;
import EmployeeManagementSystem.service.TimesheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/employee/timesheet")
@RequiredArgsConstructor
public class TimesheetController {

    private final TimesheetService service;
    private final ProjectService projectService;

    // ===================== OPEN TIMESHEET PAGE =====================

    @GetMapping("/log")
    public String openTimesheet(Model model) {

        Timesheet sheet = new Timesheet();
        sheet.setDate(LocalDate.now());
        model.addAttribute("timesheet", sheet);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmpId = auth.getName();

        List<Timesheet> myLogs = service.getTimesheetsByEmployee(currentEmpId);
        model.addAttribute("myLogs", myLogs);

        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);

        return "timesheet-log";
    }

    // ===================== SAVE TIMESHEET =====================

    @PostMapping("/submit")
    public String saveTimeSheet(@ModelAttribute("timesheet") Timesheet timesheet) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        timesheet.setEmployeeId(auth.getName());
        timesheet.setEmployeeName(auth.getName());

        service.saveTimesheet(timesheet);

        return "redirect:/employee/timesheet/log?success";
    }


//    @PostMapping("/submit")
//    public String saveTimeSheet(@ModelAttribute("timesheet") Timesheet timesheet) {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        timesheet.setEmployeeId(auth.getName());
//        timesheet.setEmployeeName(auth.getName());
//
//        // Fetch complete project from database
//        Project project = projectService.getProjectById(timesheet.getProject().getId());
//
//        // Save project relation
//        timesheet.setProject(project);
//
//        // Save project name in timesheet table
//        timesheet.setProjectName(project.getProjectName());
//
//        service.saveTimesheet(timesheet);
//
//        return "redirect:/employee/timesheet/log?success";
//    }


    // ===================== VIEW TIMESHEET =====================

    @GetMapping("/view/{id}")
    public String viewTimesheet(@PathVariable Long id,
                                Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmpId = auth.getName();

        Timesheet timesheet = service.getById(id);

        if (!timesheet.getEmployeeId().equals(currentEmpId)) {
            return "redirect:/employee/timesheet/log";
        }

        model.addAttribute("timesheet", timesheet);

        return "timesheet-view";
    }

    // ===================== EDIT PAGE =====================

    @GetMapping("/edit/{id}")
    public String editTimesheet(@PathVariable Long id,
                                Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmpId = auth.getName();

        Timesheet timesheet = service.getById(id);

        if (!timesheet.getEmployeeId().equals(currentEmpId)) {
            return "redirect:/employee/timesheet/log";
        }

        model.addAttribute("timesheet", timesheet);
        model.addAttribute("projects", projectService.getAllProjects());

        return "timesheet-edit";
        return "redirect:/employee/timesheet/log?success";
    }

    // ===================== UPDATE TIMESHEET =====================

//    @PostMapping("/update")
//    public String updateTimesheet(@ModelAttribute Timesheet timesheet) {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String currentEmpId = auth.getName();
//
//        Timesheet oldTimesheet = service.getById(timesheet.getId());
//
//        if (!oldTimesheet.getEmployeeId().equals(currentEmpId)) {
//            return "redirect:/employee/timesheet/log";
//        }
//
//        oldTimesheet.setDate(timesheet.getDate());
//        oldTimesheet.setHoursWorked(timesheet.getHoursWorked());
//        oldTimesheet.setTaskDescription(timesheet.getTaskDescription());
//        oldTimesheet.setWorkMode(timesheet.getWorkMode());
//        Project project = projectService.getProjectById(timesheet.getProject().getId());
//
//        oldTimesheet.setProject(project);
//        oldTimesheet.setProjectName(project.getProjectName());
//
//        service.saveTimesheet(oldTimesheet);
//
//        return "redirect:/employee/timesheet/log?updated";
//    }




    // ===================== UPDATE TIMESHEET =====================

    @PostMapping("/update")
    public String updateTimesheet(@ModelAttribute("timesheet") Timesheet timesheet) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmpId = auth.getName();

        // Get existing timesheet
        Timesheet oldTimesheet = service.getById(timesheet.getId());

        // Security check
        if (!oldTimesheet.getEmployeeId().equals(currentEmpId)) {
            return "redirect:/employee/timesheet/log";
        }

        // Update editable fields
        oldTimesheet.setDate(timesheet.getDate());
        oldTimesheet.setHoursWorked(timesheet.getHoursWorked());
        oldTimesheet.setTaskDescription(timesheet.getTaskDescription());
        oldTimesheet.setWorkMode(timesheet.getWorkMode());

        // Save updated timesheet
        service.saveTimesheet(oldTimesheet);

        // Redirect to Timesheet Log page
        return "redirect:/employee/timesheet/log?updated";
    }






    // ===================== HR / ADMIN =====================

    @GetMapping("/manage")
    public String manageTimesheet(Model model) {

        List<Timesheet> allLogs = service.getAllTimesheet();
        model.addAttribute("allLogs", allLogs);

        return "timesheet-manage";
    }
    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String action) {

        service.updateTimesheetStatus(id, action);

        return "redirect:/employee/timesheet/manage";
    }

}