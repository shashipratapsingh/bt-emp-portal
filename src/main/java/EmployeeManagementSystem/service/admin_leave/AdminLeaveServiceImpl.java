package EmployeeManagementSystem.service.admin_leave;

import EmployeeManagementSystem.dto.LeaveBalanceAdjustDTO;
import EmployeeManagementSystem.dto.LeaveRequestDTO;
import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.Holiday;
import EmployeeManagementSystem.entity.LeaveRequest;
import EmployeeManagementSystem.entity.admin_leave.*;
import EmployeeManagementSystem.enums.LeaveStatus;
import EmployeeManagementSystem.repository.EmployeeRepository;
import EmployeeManagementSystem.repository.HolidayRepository;
import EmployeeManagementSystem.repository.admin_leave.*;
import EmployeeManagementSystem.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
@Slf4j
public class AdminLeaveServiceImpl implements AdminLeaveService {

    @Autowired
    private LeaveTypeRepository leaveTypeRepo;
    @Autowired
    private LeavePolicyRepository leavePolicyRepo;
    @Autowired
    private HolidayRepository holidayRepo;
    @Autowired
    private LeaveRequestRepository leaveRequestRepo;
    @Autowired
    private LeaveBalanceRepository leaveBalanceRepo;
    @Autowired
    private EmployeeRepository employeeRepo;
    @Autowired
    private SecurityUtils securityUtils;

    // ------------------- Leave Types -------------------
    @Override
    public List<LeaveType> findAllLeaveTypes() {
        return leaveTypeRepo.findAll();
    }

    @Override
    public LeaveType saveLeaveType(LeaveType type) {
        if (type.getId() == null && leaveTypeRepo.existsByCode(type.getCode())) {
            throw new IllegalArgumentException("Leave type code already exists: " + type.getCode());
        }
        return leaveTypeRepo.save(type);
    }

    @Override
    public void deleteLeaveType(Long id) {
        if (!leavePolicyRepo.findByLeaveTypeId(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete: leave type has associated policies");
        }
        leaveTypeRepo.deleteById(id);
    }

    // ------------------- Leave Policies -------------------
    @Override
    public List<LeavePolicy> findAllPolicies() {
        return leavePolicyRepo.findAll();
    }

    @Override
    public LeavePolicy savePolicy(LeavePolicy policy) {
        if (!leaveTypeRepo.existsById(policy.getLeaveType().getId())) {
            throw new IllegalArgumentException("Invalid leave type");
        }
        return leavePolicyRepo.save(policy);
    }

    // ------------------- Holidays -------------------
    @Override
    public List<Holiday> findAllHolidays() {
        return holidayRepo.findAllByOrderByHolidayDateAsc();
    }

    @Override
    public Holiday saveHoliday(Holiday holiday) {
        if (holiday.getId() == null && holidayRepo.existsByHolidayDate(holiday.getHolidayDate())) {
            throw new IllegalArgumentException("Holiday already exists on this date");
        }
        return holidayRepo.save(holiday);
    }

    @Override
    public void deleteHoliday(Long id) {
        holidayRepo.deleteById(id);
    }

    // ------------------- Leave Request (Employee apply) -------------------
    @Override
    public LeaveRequest applyForLeave(LeaveRequestDTO dto) {
        Employee employee = securityUtils.getCurrentEmployee();
        Long employeeId = employee.getId(); // ✅ Long, not String

        // Resolve LeaveType by name
        LeaveType leaveType = leaveTypeRepo.findByName(dto.getLeaveTypeName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid leave type name: " + dto.getLeaveTypeName()));

        // Validate dates
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        long days = ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) + 1;
        if (days <= 0) {
            throw new IllegalArgumentException("Invalid date range");
        }

        // Check overlapping leaves – repository method must take Long employeeId
        boolean overlapping = leaveRequestRepo.existsByEmployeeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                String.valueOf(employeeId), dto.getEndDate(), dto.getStartDate());
        if (overlapping) {
            throw new IllegalArgumentException("You have an existing leave request in this period");
        }

        // Check balance for the year
        Integer year = dto.getStartDate().getYear();
        LeaveBalance balance = leaveBalanceRepo
                .findByEmployeeIdAndLeaveTypeIdAndYear(employeeId, leaveType.getId(), year) // ✅ Long
                .orElseThrow(() -> new IllegalArgumentException("No leave balance found for this type in year " + year));

        if (balance.getBalance() < days) {
            throw new IllegalArgumentException("Insufficient leave balance. Available: " + balance.getBalance());
        }

        // Create and save request
        LeaveRequest request = new LeaveRequest();
        request.setEmployeeId(employeeId.toString()); // Store as String (if your entity uses String)
        request.setEmployeeName(employee.getFullName());
        request.setLeaveType(leaveType.getName());
        request.setStartDate(dto.getStartDate());
        request.setEndDate(dto.getEndDate());
        request.setReason(dto.getReason());
        request.setStatus(LeaveStatus.PENDING);

        return leaveRequestRepo.save(request);
    }

    // ------------------- Admin: Approve / Reject -------------------
    @Override
    public LeaveRequest approveLeave(Long requestId, String comment) {
        LeaveRequest request = leaveRequestRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Leave request already processed");
        }

        // Resolve leave type ID from name
        LeaveType leaveType = leaveTypeRepo.findByName(request.getLeaveType())
                .orElseThrow(() -> new IllegalStateException("Leave type not found: " + request.getLeaveType()));

        // Fetch the employee to get the Long ID (since request stores employeeId as String)
        Long employeeId = Long.parseLong(request.getEmployeeId());
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalStateException("Employee not found for ID: " + employeeId));

        // Deduct balance
        Integer year = request.getStartDate().getYear();
        LeaveBalance balance = leaveBalanceRepo
                .findByEmployeeIdAndLeaveTypeIdAndYear(employee.getId(), leaveType.getId(), year) // ✅ Long
                .orElseThrow(() -> new IllegalStateException("Balance not found"));

        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        if (balance.getBalance() < days) {
            throw new IllegalStateException("Insufficient balance at approval time");
        }

        balance.setBalance(balance.getBalance() - days);
        leaveBalanceRepo.save(balance);

        request.setStatus(LeaveStatus.APPROVED);
        return leaveRequestRepo.save(request);
    }

    @Override
    public LeaveRequest rejectLeave(Long requestId, String comment) {
        LeaveRequest request = leaveRequestRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Leave request already processed");
        }

        request.setStatus(LeaveStatus.REJECTED);
        return leaveRequestRepo.save(request);
    }

    // ------------------- Manual Balance Adjustment (Super Admin) -------------------
    @Override
    public void adjustLeaveBalance(LeaveBalanceAdjustDTO dto) {
        Employee employee = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        LeaveType leaveType = leaveTypeRepo.findById(dto.getLeaveTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Leave type not found"));

        if (dto.getDelta() == 0) {
            throw new IllegalArgumentException("Adjustment must be non-zero");
        }

        Long employeeId = employee.getId(); // ✅ Long
        LeaveBalance balance = leaveBalanceRepo
                .findByEmployeeIdAndLeaveTypeIdAndYear(employeeId, leaveType.getId(), dto.getYear()) // ✅ Long
                .orElseGet(() -> {
                    LeaveBalance newBal = new LeaveBalance();
                    newBal.setEmployee(employee);
                    newBal.setLeaveType(leaveType);
                    newBal.setYear(dto.getYear());
                    newBal.setBalance(0.0);
                    return newBal;
                });

        double newBalance = balance.getBalance() + dto.getDelta();
        if (newBalance < 0) {
            throw new IllegalArgumentException("Balance cannot become negative");
        }
        balance.setBalance(newBalance);
        leaveBalanceRepo.save(balance);
    }

    // ------------------- Admin: List requests by status -------------------
    @Override
    public List<LeaveRequest> findRequestsByStatus(String status) {
        if (status == null || status.equalsIgnoreCase("ALL")) {
            return leaveRequestRepo.findAll();
        }
        try {
            LeaveStatus enumStatus = LeaveStatus.valueOf(status.toUpperCase());
            return leaveRequestRepo.findByStatus(enumStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }


    // ------------------- Get employee balances -------------------
    @Override
    public List<LeaveBalance> getEmployeeBalances(String employeeId) {
        // Use Spring Data method: findByEmployee_Id(Long)
        return leaveBalanceRepo.findByEmployeeId(employeeId);
    }
}