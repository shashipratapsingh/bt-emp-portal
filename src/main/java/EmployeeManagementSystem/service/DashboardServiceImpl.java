package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.AnniversaryDTO;
import EmployeeManagementSystem.dto.BirthdayDTO;
import EmployeeManagementSystem.dto.DashboardStatsDTO;
import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.repository.DepartmentRepository;
import EmployeeManagementSystem.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public DashboardServiceImpl(EmployeeRepository employeeRepository,
                                DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    // ================= DASHBOARD STATS =================
    @Override
    public DashboardStatsDTO getDashboardStats() {

        DashboardStatsDTO dto = new DashboardStatsDTO();

        dto.setTotalEmployees(employeeRepository.count());
        dto.setDepartments(departmentRepository.count());

        dto.setAttendanceRate(95.0);
        dto.setTotalPayrollCost(employeeRepository.count() * 70000.0);

        dto.setJoined(Arrays.asList(5, 8, 6, 10, 7, 12, 9));
        dto.setLeft(Arrays.asList(1, 2, 1, 3, 2, 2, 1));
        dto.setAttendanceTrend(Arrays.asList(92, 95, 94, 96, 93, 97, 98));

        return dto;
    }

    // ================= BIRTHDAY DTO =================
    @Override
    public List<BirthdayDTO> getUpcomingBirthdaysDTO() {

        LocalDate today = LocalDate.now();
        List<BirthdayDTO> list = new ArrayList<>();

        for (Employee e : employeeRepository.findAll()) {

            if (e.getDateOfBirth() == null) continue;

            LocalDate nextBirthday = e.getDateOfBirth().withYear(today.getYear());

            if (nextBirthday.isBefore(today)) {
                nextBirthday = nextBirthday.plusYears(1);
            }

            long days = ChronoUnit.DAYS.between(today, nextBirthday);

            BirthdayDTO dto = new BirthdayDTO();
            dto.setName(e.getFirstName());
            dto.setDateOfBirth(e.getDateOfBirth());
            dto.setRemainingDays(days);
            dto.setNextDate(nextBirthday);

            list.add(dto);
        }

        list.sort(Comparator.comparingLong(BirthdayDTO::getRemainingDays));
        return list;
    }

    // ================= ANNIVERSARY DTO =================
    @Override
    public List<AnniversaryDTO> getUpcomingAnniversariesDTO() {

        LocalDate today = LocalDate.now();
        List<AnniversaryDTO> list = new ArrayList<>();

        for (Employee e : employeeRepository.findAll()) {

            if (e.getJoiningDate() == null) continue;

            LocalDate nextAnniversary = e.getJoiningDate().withYear(today.getYear());

            if (nextAnniversary.isBefore(today)) {
                nextAnniversary = nextAnniversary.plusYears(1);
            }

            long days = ChronoUnit.DAYS.between(today, nextAnniversary);

            AnniversaryDTO dto = new AnniversaryDTO();
            dto.setName(e.getFirstName());
            dto.setJoiningDate(e.getJoiningDate());
            dto.setRemainingDays(days);
            dto.setNextDate(nextAnniversary);

            list.add(dto);
        }

        list.sort(Comparator.comparingLong(AnniversaryDTO::getRemainingDays));
        return list;
    }
    // ================= RAW METHODS (FIXED - SIMPLE & SAFE) =================
    @Override
    public List<Employee> getUpcomingBirthdaysRaw() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> getUpcomingAnniversariesRaw() {
        return employeeRepository.findAll();
    }

    // ================= LEGACY METHODS (FIXED - NO DOUBLE LOOP) =================
    @Override
    public List<Employee> getUpcomingBirthdays() {
        return employeeRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(e -> {
                    LocalDate today = LocalDate.now();
                    if (e.getDateOfBirth() == null) return LocalDate.MAX;

                    LocalDate next = e.getDateOfBirth().withYear(today.getYear());
                    if (next.isBefore(today)) next = next.plusYears(1);

                    return next;
                }))
                .toList();
    }

    @Override
    public List<Employee> getUpcomingAnniversaries() {
        return employeeRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(e -> {
                    LocalDate today = LocalDate.now();
                    if (e.getJoiningDate() == null) return LocalDate.MAX;

                    LocalDate next = e.getJoiningDate().withYear(today.getYear());
                    if (next.isBefore(today)) next = next.plusYears(1);

                    return next;
                }))
                .toList();
    }

    // ================= PAGINATION =================
    @Override
    public Page<Employee> getUpcomingBirthdays(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Page<Employee> getUpcomingAnniversaries(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findAll(pageable);
    }
}