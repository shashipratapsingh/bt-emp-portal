package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.WfhRequest;
import EmployeeManagementSystem.enums.WorkMode;
import EmployeeManagementSystem.repository.WfhRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WfhServiceImpl implements WfhService{
    private final WfhRequestRepository repository;
    @Override
    public WfhRequest saveRequest(WfhRequest request){
        request.setStatus("PENDING");
        return repository.save(request);
    }
    @Override
    public List<WfhRequest> getWFHEmployees() {

        LocalDate today = LocalDate.now();

        return repository.findAll()
                .stream()
                .filter(request ->
                        "APPROVED".equals(request.getStatus()) &&
                                !today.isBefore(request.getStartDate()) &&
                                !today.isAfter(request.getEndDate()))
                .toList();
    }
}
