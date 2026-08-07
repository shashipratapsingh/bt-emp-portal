package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.BirthdayDTO;
import EmployeeManagementSystem.dto.DashboardStatsDTO;
import EmployeeManagementSystem.dto.AnniversaryDTO;
import EmployeeManagementSystem.dto.dynamic.*;
import EmployeeManagementSystem.entity.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DashboardService {

    DashboardStatsDTO getDashboardStats();

    // ===== DTO BASED (USE THIS IN DASHBOARD UI) =====
    List<BirthdayDTO> getUpcomingBirthdaysDTO();

    List<AnniversaryDTO> getUpcomingAnniversariesDTO();

    // ===== ENTITY BASED (OPTIONAL - REMOVE IF NOT NEEDED) =====
    List<Employee> getUpcomingBirthdaysRaw();

    List<Employee> getUpcomingAnniversariesRaw();

    // ================= RAW METHODS (OPTIONAL) =================
    List<Employee> getUpcomingBirthdays();

    List<Employee> getUpcomingAnniversaries();

    // ===== PAGINATION (ADMIN TABLE VIEW) =====
    Page<Employee> getUpcomingBirthdays(int page, int size);

    Page<Employee> getUpcomingAnniversaries(int page, int size);

    YearlyRevenueDTO getYearlyRevenue(int year);

    MonthlyRevenueDTO getMonthlyRevenue(int year, int month);

    List<DepartmentRevenueDTO> getDepartmentRevenue(int year);

    OnboardingProjectsDTO getOnboardingMonthly();

    OnboardingProjectsDTO getOnboardingYearly();

    // ---------- Project Loss ----------
    ProjectLossDTO getProjectLossMonthly();

    ProjectLossDTO getProjectLossYearly();


    List<TechnologyRevenueDTO> getTechMonthlyRevenue(int year, int month);

    TechnologyRevenueTotalDTO getTechMonthlyTotal(int year, int month);

    List<TechnologyRevenueDTO> getTechYearlyRevenue(int year);

    TechnologyRevenueTotalDTO getTechYearlyTotal(int year);

    ProjectOverviewDTO getProjectOverview(int year);
}