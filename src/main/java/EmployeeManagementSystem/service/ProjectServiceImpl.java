package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.EmployeeProfile;
import EmployeeManagementSystem.entity.Project;
import EmployeeManagementSystem.enums.ProjectStatus;
import EmployeeManagementSystem.repository.ProjectRepository;
import EmployeeManagementSystem.repository.EmployeeProfileRepository;
import EmployeeManagementSystem.repository.EmployeeRepository;
import EmployeeManagementSystem.service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeProfileService employeeProfileService;  // Added this

    @Override
    public Project saveProject(Project project) {
        // Generate project name if not set
        if (project.getProjectName() == null || project.getProjectName().isEmpty()) {
            String employeeName = "";
            if (project.getAssignedEmployeeId() != null) {
                try {
                    EmployeeProfile employeeProfile = employeeProfileService.getEmployeeProfileById(project.getAssignedEmployeeId());
                    if (employeeProfile != null) {
                        employeeName = employeeProfile.getFullName().toUpperCase();
                    }
                } catch (Exception e) {
                    // Employee not found, continue with empty name
                }
            }

            String region = project.getClientRegion() != null ? project.getClientRegion() : "";
            String client = project.getClientName() != null ? project.getClientName().toUpperCase() : "";

            // Generate project name
            String generatedName = employeeName + "-" + region + "-" + client;
            project.setProjectName(generatedName);
        }

        // Set created timestamp if not set
        if (project.getCreatedAt() == null) {
            project.setCreatedAt(LocalDateTime.now());
        }

        // Set status if not set
        if (project.getStatus() == null) {
            project.setStatus(ProjectStatus.ACTIVE);
        }

        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Long id, Project project) {
        Project existingProject = getProjectById(id);

        // Update fields
        existingProject.setProjectName(project.getProjectName());
        existingProject.setClientName(project.getClientName());
        existingProject.setClientRegion(project.getClientRegion());
        existingProject.setDepartmentId(project.getDepartmentId());
        existingProject.setAssignedEmployeeId(project.getAssignedEmployeeId());
        existingProject.setAssignedEmployeeName(project.getAssignedEmployeeName());
        existingProject.setProjectType(project.getProjectType());
        existingProject.setTotalCost(project.getTotalCost());
        existingProject.setOnboardingDate(project.getOnboardingDate());
        existingProject.setDescription(project.getDescription());
        existingProject.setStatus(project.getStatus());

        // Update timestamp
        existingProject.setUpdatedAt(LocalDateTime.now());

        // Update assigned employees if needed
        if (project.getAssignedEmployees() != null) {
            existingProject.setAssignedEmployees(project.getAssignedEmployees());
        }

        return projectRepository.save(existingProject);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public Project getProjectById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public List<Project> getAssignedProjects() {
        return projectRepository.findByAssignedEmployeesIsNotEmpty();
    }

    @Override
    public List<Project> getAllProjectsWithStatus() {
        return projectRepository.findAllByOrderByStatusAsc();
    }

    @Override
    public void assignEmployeesToProject(Long projectId, List<Long> employeeIds) {
        Project project = getProjectById(projectId);
        List<Employee> employees = employeeRepository.findAllById(employeeIds);
        project.getAssignedEmployees().clear();
        project.getAssignedEmployees().addAll(employees);

        // Also set the primary assigned employee if employees list is not empty
        if (!employees.isEmpty()) {
            project.setAssignedEmployeeId(employees.get(0).getId());
            project.setAssignedEmployeeName(employees.get(0).getFullName());
        }

        project.setUpdatedAt(LocalDateTime.now());
        projectRepository.save(project);
    }
}