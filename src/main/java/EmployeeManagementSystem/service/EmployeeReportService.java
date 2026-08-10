// EmployeeReportService.java
package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.EmployeeReportDTO;
import EmployeeManagementSystem.entity.Department;
import EmployeeManagementSystem.entity.EmployeeProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeReportService {

    Page<EmployeeReportDTO> getEmployeeReports(String keyword, Long departmentId,
                                               String status, Pageable pageable);

    EmployeeReportDTO convertToEmployeeReportDTO(EmployeeProfile profile);

    EmployeeReportDTO getEmployeeDetail(Long profileId);

    String getCurrentActiveProject(EmployeeProfile profile);

    List<Department> getAllDepartments();
}