package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.ProjectOffLog;
import EmployeeManagementSystem.repository.ProjectOffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectOffServiceImpl implements ProjectOffService{
    private final ProjectOffRepository repository;

    public List<ProjectOffLog> getTodayProjectOffLogs() {

        LocalDate today = LocalDate.now();

        return repository.findAll()
                .stream()
                .filter(log ->
                        !today.isBefore(log.getFromDate()) &&
                                !today.isAfter(log.getToDate()))
                .toList();

    }
}
