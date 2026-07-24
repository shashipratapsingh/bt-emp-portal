package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.entity.EmployeeProfile;
import EmployeeManagementSystem.entity.Project;
import EmployeeManagementSystem.enums.ProjectStatus;
import EmployeeManagementSystem.service.DepartmentService;
import EmployeeManagementSystem.service.EmployeeProfileService;
import EmployeeManagementSystem.service.EmployeeService;
import EmployeeManagementSystem.service.ProjectService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/admin/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeProfileService employeeProfileService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/add")
    public String showAddProjectForm(Model model) {
        System.out.println("=== LOADING ADD PROJECT PAGE ===");

        // Add a new project object
        model.addAttribute("project", new Project());

        // Fetch all departments from database
        model.addAttribute("departments", departmentService.getAllDepartments());

        // Fetch all employee profiles (initially)
        Object allEmployees = employeeProfileService.getAllEmployeeProfiles();
        if (allEmployees != null) {
            model.addAttribute("employees", allEmployees);
        } else {
            model.addAttribute("employees", new ArrayList<>());
        }

        // Add statuses
        model.addAttribute("statuses", ProjectStatus.values());

        // Fetch all projects for the list
        List<Project> projects = projectService.getAllProjects();
        System.out.println("=== TOTAL PROJECTS FOUND: " + projects.size());

        // Log each project for debugging
        for (Project p : projects) {
            System.out.println("Project ID: " + p.getId() +
                    ", Name: " + p.getProjectName() +
                    ", Client: " + p.getClientName() +
                    ", Status: " + p.getStatus());
        }

        model.addAttribute("projects", projects);

        return "admin/project-management/add-project";
    }

    @PostMapping("/save")
    public String saveProject(@Valid @ModelAttribute("project") Project project,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        System.out.println("=== SAVE PROJECT METHOD CALLED ===");
        System.out.println("=== Project data: " + project);
        System.out.println("=== Project name: " + project.getProjectName());
        System.out.println("=== Assigned Employee ID: " + project.getAssignedEmployeeId());

        if (result.hasErrors()) {
            System.out.println("=== VALIDATION ERRORS ===");
            result.getAllErrors().forEach(error -> {
                System.out.println("Error: " + error.getDefaultMessage());
            });

            model.addAttribute("departments", departmentService.getAllDepartments());
            Object allEmployees = employeeProfileService.getAllEmployeeProfiles();
            if (allEmployees != null) {
                model.addAttribute("employees", allEmployees);
            } else {
                model.addAttribute("employees", new ArrayList<>());
            }
            model.addAttribute("statuses", ProjectStatus.values());
            model.addAttribute("projects", projectService.getAllProjects());
            return "admin/project-management/add-project";
        }

        try {
            // Set employee name if assigned - using EmployeeProfileService
            if (project.getAssignedEmployeeId() != null) {
                try {
                    EmployeeProfile employeeProfile = employeeProfileService.getEmployeeProfileById(project.getAssignedEmployeeId());
                    if (employeeProfile != null) {
                        project.setAssignedEmployeeName(employeeProfile.getFullName());
                        System.out.println("Set assigned employee name: " + employeeProfile.getFullName());
                    }
                } catch (Exception e) {
                    System.out.println("Error fetching employee: " + e.getMessage());
                    project.setAssignedEmployeeName(null);
                }
            }

            // Set default values if needed
            if (project.getStatus() == null) {
                project.setStatus(ProjectStatus.ACTIVE);
            }

            if (project.getCreatedAt() == null) {
                project.setCreatedAt(LocalDateTime.now());
            }

            Project savedProject = projectService.saveProject(project);
            System.out.println("=== PROJECT SAVED SUCCESSFULLY ===");
            System.out.println("ID: " + savedProject.getId());
            System.out.println("Name: " + savedProject.getProjectName());

            redirectAttributes.addFlashAttribute("successMessage",
                    "Project created successfully! Name: " + savedProject.getProjectName());
            return "redirect:/admin/projects/add";
        } catch (Exception e) {
            System.err.println("=== ERROR SAVING PROJECT ===");
            e.printStackTrace();

            model.addAttribute("errorMessage", "Error saving project: " + e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            Object allEmployees = employeeProfileService.getAllEmployeeProfiles();
            if (allEmployees != null) {
                model.addAttribute("employees", allEmployees);
            } else {
                model.addAttribute("employees", new ArrayList<>());
            }
            model.addAttribute("statuses", ProjectStatus.values());
            model.addAttribute("projects", projectService.getAllProjects());
            return "admin/project-management/add-project";
        }
    }

    @GetMapping("/seed")
    @ResponseBody
    public String seedSampleProjects() {
        try {
            int existingCount = projectService.getAllProjects().size();
            if (existingCount >= 5) {
                return "Already have " + existingCount + " projects. Total: " + existingCount;
            }

            createSampleProjects();
            int newCount = projectService.getAllProjects().size();
            return "Successfully seeded sample projects! Total now: " + newCount;
        } catch (Exception e) {
            return "Error seeding projects: " + e.getMessage();
        }
    }

    private void createSampleProjects() {
        List<Project> existing = projectService.getAllProjects();
        List<String> existingNames = existing.stream()
                .map(Project::getProjectName)
                .collect(java.util.stream.Collectors.toList());

        Object[][] sampleData = {
                {"E-Commerce-Platform-IND-Flipkart", "Flipkart", "IND", 1L, 1L, "Rahul Kumar", "C2C", 1500000.0, "Full-stack e-commerce platform development"},
                {"Mobile-App-USA-Google", "Google", "USA", 2L, 2L, "Priya Sharma", "INDIVIDUAL", 850000.0, "Android mobile application development"},
                {"CRM-System-UK-Salesforce", "Salesforce", "UK", 1L, 3L, "Amit Patel", "TEAM", 2000000.0, "Enterprise CRM system implementation"},
                {"Data-Analytics-UAE-DubaiGov", "Dubai Government", "UAE", 3L, 4L, "Sneha Reddy", "C2C", 1200000.0, "Smart city data analytics platform"},
                {"Cloud-Migration-AUS-AWS", "Amazon Web Services", "AUS", 2L, 5L, "Vikram Singh", "TEAM", 3000000.0, "Cloud migration and infrastructure setup"}
        };

        for (Object[] data : sampleData) {
            String projectName = (String) data[0];
            if (existingNames.contains(projectName)) {
                continue;
            }

            Project project = new Project();
            project.setProjectName(projectName);
            project.setClientName((String) data[1]);
            project.setClientRegion((String) data[2]);
            project.setDepartmentId((Long) data[3]);
            project.setAssignedEmployeeId((Long) data[4]);
            project.setAssignedEmployeeName((String) data[5]);
            project.setProjectType((String) data[6]);
            project.setTotalCost((Double) data[7]);
            project.setOnboardingDate(LocalDate.now().minusDays((long) (Math.random() * 60)));
            project.setStatus(ProjectStatus.ACTIVE);
            project.setDescription((String) data[8]);
            project.setCreatedAt(LocalDateTime.now());

            projectService.saveProject(project);
            System.out.println("Seeded project: " + projectName);
        }
    }

    @GetMapping
    public String listProjects(Model model) {
        List<Project> projects = projectService.getAllProjects();
        System.out.println("=== LISTING PROJECTS: " + projects.size());
        model.addAttribute("projects", projects);
        return "admin/project-management/projects-list";
    }

    // Get employee profiles by department name (for AJAX calls)
    @GetMapping("/employees/by-department/{departmentId}")
    @ResponseBody
    public List<Map<String, Object>> getEmployeesByDepartment(@PathVariable Long departmentId) {
        System.out.println("=== Fetching employee profiles for department ID: " + departmentId);
        // Get department name from ID
        String departmentName = departmentService.getDepartmentById(departmentId).getDepartmentName();
        System.out.println("=== Department Name: " + departmentName);

        List<EmployeeProfile> employeeProfiles = employeeProfileService.getEmployeeProfilesByDepartment(departmentName);
        System.out.println("=== Found " + employeeProfiles.size() + " employee profiles");

        List<Map<String, Object>> result = new ArrayList<>();

        for (EmployeeProfile emp : employeeProfiles) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", emp.getId());
            map.put("fullName", emp.getFullName());
            result.add(map);
            System.out.println("=== Employee: " + emp.getId() + " - " + emp.getFullName());
        }

        return result;
    }
}