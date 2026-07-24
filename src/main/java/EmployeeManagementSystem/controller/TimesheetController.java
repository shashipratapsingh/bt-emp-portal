package EmployeeManagementSystem.controller;

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
    @GetMapping("/log")
    public String OpenTimesheet(Model model){
        Timesheet sheet = new Timesheet();
        sheet.setDate(LocalDate.now());
        model.addAttribute("timesheet", sheet);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmpId = auth.getName();

        List<Timesheet> myLogs = service.getTimesheetsByEmployee(currentEmpId);
        model.addAttribute("myLogs", myLogs);
        return "timesheet-log";
    }
    @PostMapping("/submit")
    public String saveTimeSheet(@ModelAttribute("timesheet")Timesheet timesheet){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        timesheet.setEmployeeName(auth.getName());
        timesheet.setEmployeeId(auth.getName());
        service.saveTimesheet(timesheet);
        return "redirect:/timesheet/log";
    }
    @GetMapping("/manage")
    public String manageTimesheet(Model model){
        List<Timesheet> allLogs=service.getAllTimesheet();
        model.addAttribute("allLogs",allLogs);
        return "timesheet-manage";
    }
    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable("id") Long id,@RequestParam("action") String action){
        service.updateTimesheetStatus(id,action);
        return "redirect:/timesheet/manage";
    }

}
