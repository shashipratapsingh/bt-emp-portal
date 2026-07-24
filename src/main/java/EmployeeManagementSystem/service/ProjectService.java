package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.Project;

import java.util.List;

public interface ProjectService {
    Project saveProject(Project project);
    Project updateProject(Long id, Project project);
    void deleteProject(Long id);
    Project getProjectById(Long id);
    List<Project> getAllProjects();
    List<Project> getAssignedProjects();
    List<Project> getAllProjectsWithStatus();
    void assignEmployeesToProject(Long projectId, List<Long> employeeIds);

}
