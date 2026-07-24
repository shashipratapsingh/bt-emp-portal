package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.ProjectOffLog;

import java.util.List;

public interface ProjectOffService {
    List<ProjectOffLog> getTodayProjectOffLogs();
}
