package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.LeaveRequest;
import EmployeeManagementSystem.enums.LeaveStatus;
import EmployeeManagementSystem.entity.RegisterEmployee;
import EmployeeManagementSystem.repository.LeaveRepository;
import EmployeeManagementSystem.repository.RegisterEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService{
    private final RegisterEmployeeRepository repository;
    private final LeaveRepository leaveRepository;

    @Override
     public List<LeaveRequest> getLeavesByEmployeeId(String employeeId){
        return leaveRepository.findByEmployeeId(employeeId);
    }
    @Override
    public LeaveRequest applyLeave(LeaveRequest leaveRequest){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String currentEmpId=authentication.getName();
        RegisterEmployee employee=repository.findByUserId(currentEmpId);
        leaveRequest.setEmployeeId(currentEmpId);
        leaveRequest.setEmployeeName(employee.getName());
        return leaveRepository.save(leaveRequest);
    }
    @Override
    public List<LeaveRequest> getAllLeaveRequest(){
        return leaveRepository.findAll();
    }
    @Override
    public void updateleaveStatus(Long id, String action) {

        LeaveRequest lr = leaveRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid leave request id: " + id));

        if ("approve".equalsIgnoreCase(action)) {
            lr.setStatus(LeaveStatus.APPROVED);
        }
        else if ("reject".equalsIgnoreCase(action)) {
            lr.setStatus(LeaveStatus.REJECTED);
        }

        leaveRepository.save(lr);
    }

    @Override
    public List<LeaveRequest> getAllLeaves() {
        return leaveRepository.findAll();
    }

}
