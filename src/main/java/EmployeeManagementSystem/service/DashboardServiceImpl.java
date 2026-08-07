package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.AnniversaryDTO;
import EmployeeManagementSystem.dto.BirthdayDTO;
import EmployeeManagementSystem.dto.DashboardStatsDTO;
import EmployeeManagementSystem.dto.dynamic.*;
import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.Project;
import EmployeeManagementSystem.enums.ProjectStatus;
import EmployeeManagementSystem.repository.DepartmentRepository;
import EmployeeManagementSystem.repository.EmployeeRepository;
import EmployeeManagementSystem.repository.ProjectRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public DashboardServiceImpl(EmployeeRepository employeeRepository,
                                DepartmentRepository departmentRepository,
                                ProjectRepository projectRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
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
            dto.setDob(e.getDateOfBirth());
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

    @Override
    public YearlyRevenueDTO getYearlyRevenue(int year) {
        if (year < 2000) {
            YearlyRevenueDTO empty = new YearlyRevenueDTO();
            empty.setTotalRevenue(BigDecimal.ZERO);
            empty.setMonthlyRevenue(new ArrayList<>(Collections.nCopies(12, BigDecimal.ZERO)));
            empty.setPercentageChange(0.0);
            return empty;
        }
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);

        List<Project> projects = projectRepository.findProjectsOverlappingYear(startOfYear, endOfYear);
        BigDecimal[] monthlyRevenue = new BigDecimal[12];
        Arrays.fill(monthlyRevenue, BigDecimal.ZERO);

        for (Project p : projects) {
            // Determine effective start/end within the year
            LocalDate effStart = p.getOnboardingDate().isBefore(startOfYear) ? startOfYear : p.getOnboardingDate();
            LocalDate effEnd = (p.getEndDate() != null && p.getEndDate().isBefore(endOfYear))
                    ? p.getEndDate() : endOfYear;

            if (effEnd.isBefore(startOfYear) || effStart.isAfter(endOfYear)) continue;

            long totalDays = ChronoUnit.DAYS.between(effStart, effEnd) + 1;
            BigDecimal dailyRate = BigDecimal.valueOf(p.getTotalCost()).divide(BigDecimal.valueOf(totalDays), 4, RoundingMode.HALF_UP);

            // Allocate to each month in the overlap
            LocalDate cursor = effStart;
            while (!cursor.isAfter(effEnd)) {
                int monthIndex = cursor.getMonthValue() - 1;
                LocalDate monthStart = cursor.withDayOfMonth(1);
                LocalDate monthEnd = cursor.withDayOfMonth(cursor.lengthOfMonth());
                LocalDate actualStart = monthStart.isBefore(effStart) ? effStart : monthStart;
                LocalDate actualEnd = monthEnd.isAfter(effEnd) ? effEnd : monthEnd;
                long daysInMonth = ChronoUnit.DAYS.between(actualStart, actualEnd) + 1;
                if (daysInMonth > 0) {
                    BigDecimal monthAmount = dailyRate.multiply(BigDecimal.valueOf(daysInMonth));
                    monthlyRevenue[monthIndex] = monthlyRevenue[monthIndex].add(monthAmount);
                }
                cursor = cursor.plusMonths(1).withDayOfMonth(1);
            }
        }

        BigDecimal total = calculateYearRevenue(year);

        BigDecimal prevTotal = calculateYearRevenue(year - 1);
        double pctChange = (prevTotal.compareTo(BigDecimal.ZERO) > 0)
                ? total.subtract(prevTotal).divide(prevTotal, 4, RoundingMode.HALF_UP)
                  .multiply(BigDecimal.valueOf(100)).doubleValue()
                : 0.0;

        YearlyRevenueDTO dto = new YearlyRevenueDTO();
        dto.setYear(year);
        dto.setTotalRevenue(total);
        dto.setMonthlyRevenue(Arrays.asList(monthlyRevenue));
        dto.setPercentageChange(pctChange);
        return dto;
    }

    @Override
    public MonthlyRevenueDTO getMonthlyRevenue(int year, int month) {
        if (year < 2000) {
            MonthlyRevenueDTO empty = new MonthlyRevenueDTO();
            empty.setTotalRevenue(BigDecimal.ZERO);
            empty.setPercentageChange(0.0);
            return empty;
        }
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        List<Project> projects = projectRepository.findProjectsOverlappingMonth(startOfMonth, endOfMonth);
        BigDecimal totalRevenue = BigDecimal.ZERO;

        for (Project p : projects) {
            LocalDate effStart = p.getOnboardingDate().isBefore(startOfMonth) ? startOfMonth : p.getOnboardingDate();
            LocalDate effEnd = (p.getEndDate() != null && p.getEndDate().isBefore(endOfMonth))
                    ? p.getEndDate() : endOfMonth;
            if (effEnd.isBefore(startOfMonth) || effStart.isAfter(endOfMonth)) continue;

            // Total days the project covers over its entire lifespan (for daily rate)
            long totalDaysProject = ChronoUnit.DAYS.between(
                    p.getOnboardingDate(),
                    (p.getEndDate() != null ? p.getEndDate() : LocalDate.now())
            ) + 1;
            BigDecimal dailyRate = BigDecimal.valueOf(p.getTotalCost()).divide(BigDecimal.valueOf(totalDaysProject), 4, RoundingMode.HALF_UP);

            long daysInMonth = ChronoUnit.DAYS.between(effStart, effEnd) + 1;
            totalRevenue = totalRevenue.add(dailyRate.multiply(BigDecimal.valueOf(daysInMonth)));
        }

        // Previous month revenue
        LocalDate prevMonth = startOfMonth.minusMonths(1);
        BigDecimal prevRevenue = calculateMonthRevenue(
                prevMonth.getYear(),
                prevMonth.getMonthValue()
        );
        double pctChange = (prevRevenue.compareTo(BigDecimal.ZERO) > 0)
                ? totalRevenue.subtract(prevRevenue)
                .divide(prevRevenue, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).doubleValue()
                : 0.0;

        MonthlyRevenueDTO dto = new MonthlyRevenueDTO();
        dto.setYear(year);
        dto.setMonth(month);
        dto.setTotalRevenue(totalRevenue);
        dto.setPercentageChange(pctChange);
        return dto;
    }

    @Override
    public List<DepartmentRevenueDTO> getDepartmentRevenue(int year) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

        List<Project> projects = projectRepository.findProjectsOverlappingYear(startOfYear, endOfYear);

        // Pre-fetch department names for all department IDs (cache)
        Map<Long, String> deptNameMap = new HashMap<>();
        departmentRepository.findAll().forEach(dept -> deptNameMap.put(dept.getId(), dept.getDepartmentName()));

        // Aggregation maps
        Map<Long, BigDecimal> yearlyMap = new HashMap<>();
        Map<Long, BigDecimal> monthlyMap = new HashMap<>();

        for (Project p : projects) {
            Long deptId = p.getDepartmentId(); // may be null
            if (deptId == null) continue; // skip unassigned or treat as "Unassigned"

            // 1) Yearly revenue (pro-rata within the year)
            LocalDate effStartYear = p.getOnboardingDate().isBefore(startOfYear) ? startOfYear : p.getOnboardingDate();
            LocalDate effEndYear = (p.getEndDate() != null && p.getEndDate().isBefore(endOfYear))
                    ? p.getEndDate() : endOfYear;
            if (effEndYear.isBefore(startOfYear) || effStartYear.isAfter(endOfYear)) continue;

            long daysInYear = ChronoUnit.DAYS.between(effStartYear, effEndYear) + 1;
            long totalDaysProject = ChronoUnit.DAYS.between(
                    p.getOnboardingDate(),
                    (p.getEndDate() != null ? p.getEndDate() : LocalDate.now())
            ) + 1;
            BigDecimal dailyRate = BigDecimal.valueOf(p.getTotalCost()).divide(BigDecimal.valueOf(totalDaysProject), 4, RoundingMode.HALF_UP);
            BigDecimal yearlyAmount = dailyRate.multiply(BigDecimal.valueOf(daysInYear));
            yearlyMap.merge(deptId, yearlyAmount, BigDecimal::add);

            // 2) Monthly revenue (current month)
            if (p.getOnboardingDate().isBefore(endOfMonth) &&
                    (p.getEndDate() == null || p.getEndDate().isAfter(startOfMonth))) {
                LocalDate effStartMonth = p.getOnboardingDate().isBefore(startOfMonth) ? startOfMonth : p.getOnboardingDate();
                LocalDate effEndMonth = (p.getEndDate() != null && p.getEndDate().isBefore(endOfMonth))
                        ? p.getEndDate() : endOfMonth;
                if (effEndMonth.isBefore(startOfMonth) || effStartMonth.isAfter(endOfMonth)) continue;
                long daysInMonth = ChronoUnit.DAYS.between(effStartMonth, effEndMonth) + 1;
                BigDecimal monthlyAmount = dailyRate.multiply(BigDecimal.valueOf(daysInMonth));
                monthlyMap.merge(deptId, monthlyAmount, BigDecimal::add);
            }
        }

        // Build DTO list
        List<DepartmentRevenueDTO> result = new ArrayList<>();
        for (Map.Entry<Long, BigDecimal> entry : yearlyMap.entrySet()) {
            Long deptId = entry.getKey();
            String deptName = deptNameMap.getOrDefault(deptId, "Unknown");
            DepartmentRevenueDTO dto = new DepartmentRevenueDTO();
            dto.setDepartment(deptName);
            dto.setYearlyRevenue(entry.getValue());
            dto.setMonthlyRevenue(monthlyMap.getOrDefault(deptId, BigDecimal.ZERO));
            dto.setYearlyGrowth(18.0);   // placeholder – compute from previous year if needed
            dto.setMonthlyGrowth(3.0);   // placeholder
            result.add(dto);
        }
        return result;
    }

    // Helper for consistent colors
    private String getColorForDepartment(String dept) {
        Map<String, String> colors = Map.of(
                "Java", "#1a56db",
                "Python", "#0b7e3d",
                "Salesforce", "#c2410c",
                "Magento", "#6d28d9",
                "Dynamics", "#0e7490",
                "C2C", "#1a56db",
                "TEAM", "#0b7e3d",
                "INDIVIDUAL", "#c2410c"
        );
        return colors.getOrDefault(dept, "#64748b");
    }

    @Override
    public OnboardingProjectsDTO getOnboardingMonthly() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());

        List<Project> projects = projectRepository.findProjectsByOnboardingDateBetween(start, end);
        long total = projects.size();

        BigDecimal netRevenue = projects.stream()
                .map(Project::getTotalCost)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal profit = netRevenue.multiply(BigDecimal.valueOf(0.25));
        double margin = 25.7;
        BigDecimal avg = total > 0 ? netRevenue.divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        LocalDate prevMonth = start.minusMonths(1);
        LocalDate prevStart = prevMonth.withDayOfMonth(1);
        LocalDate prevEnd = prevMonth.withDayOfMonth(prevMonth.lengthOfMonth());
        long prevCount = projectRepository.countByOnboardingDateBetween(prevStart, prevEnd);
        long newProjects = total - prevCount;

        OnboardingProjectsDTO dto = new OnboardingProjectsDTO();
        dto.setTotalProjects(total);
        dto.setNewProjects(newProjects);
        dto.setNetRevenue(netRevenue);
        dto.setProfit(profit);
        dto.setMargin(margin);
//        dto.setAvgRevenuePerProject(avg);
        return dto;
    }

    @Override
    public OnboardingProjectsDTO getOnboardingYearly() {
        LocalDate now = LocalDate.now();
        LocalDate start = LocalDate.of(now.getYear(), 1, 1);
        LocalDate end = LocalDate.of(now.getYear(), 12, 31);

        List<Project> projects = projectRepository.findProjectsByOnboardingDateBetween(start, end);
        long total = projects.size();
        BigDecimal netRevenue = projects.stream()
                .map(Project::getTotalCost)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal profit = netRevenue.multiply(BigDecimal.valueOf(0.32));
        double margin = 32.0;
        BigDecimal avg = total > 0 ? netRevenue.divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        LocalDate prevYearStart = start.minusYears(1);
        LocalDate prevYearEnd = end.minusYears(1);
        long prevCount = projectRepository.countByOnboardingDateBetween(prevYearStart, prevYearEnd);
        long newProjects = total - prevCount;

        OnboardingProjectsDTO dto = new OnboardingProjectsDTO();
        dto.setTotalProjects(total);
        dto.setNewProjects(newProjects);
        dto.setNetRevenue(netRevenue);
        dto.setProfit(profit);
        dto.setMargin(margin);
//        dto.setAvgRevenuePerProject(avg);
        return dto;
    }

    // ---------- Project Loss ----------
    @Override
    public ProjectLossDTO getProjectLossMonthly() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());

        List<Project> lossProjects = projectRepository.findProjectsByOnboardingDateBetweenAndStatus(
                start, end, ProjectStatus.INACTIVE   // yields "INACTIVE" (matches your DB)
        );
        BigDecimal totalLoss = lossProjects.stream()
                .map(p -> BigDecimal.valueOf(p.getTotalCost()))   // Double → BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(BigDecimal.valueOf(0.1));
        // For highest/lowest, we can pick from actual data if we have loss amounts
        // Placeholder logic:
        BigDecimal highest = lossProjects.stream()
                .max(Comparator.comparing(Project::getTotalCost))
                .map(p -> BigDecimal.valueOf(p.getTotalCost()).multiply(BigDecimal.valueOf(0.1)))
                .orElse(BigDecimal.ZERO);
        String highestProject = lossProjects.stream()
                .max(Comparator.comparing(Project::getTotalCost))
                .map(Project::getProjectName)
                .orElse("N/A");

        BigDecimal lowest = lossProjects.stream()
                .min(Comparator.comparing(Project::getTotalCost))
                .map(p -> BigDecimal.valueOf(p.getTotalCost()).multiply(BigDecimal.valueOf(0.1)))
                .orElse(BigDecimal.ZERO);
        String lowestProject = lossProjects.stream()
                .min(Comparator.comparing(Project::getTotalCost))
                .map(Project::getProjectName)
                .orElse("N/A");

        ProjectLossDTO dto = new ProjectLossDTO();
        dto.setTotalLoss(totalLoss);
        dto.setHighestLossAmount(highest);
        dto.setHighestLossProject(highestProject);
        dto.setLowestLossAmount(lowest);
        dto.setLowestLossProject(lowestProject);
        return dto;
    }

    @Override
    public ProjectLossDTO getProjectLossYearly() {
        LocalDate now = LocalDate.now();
        LocalDate start = LocalDate.of(now.getYear(), 1, 1);
        LocalDate end = LocalDate.of(now.getYear(), 12, 31);

        List<Project> lossProjects = projectRepository.findProjectsByOnboardingDateBetweenAndStatus(
                start, end, ProjectStatus.INACTIVE  // yields "INACTIVE" (matches your DB)
        );
        BigDecimal totalLoss = lossProjects.stream()
                .map(p -> BigDecimal.valueOf(p.getTotalCost()))   // convert to BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(BigDecimal.valueOf(0.1));

        BigDecimal highest = lossProjects.stream()
                .max(Comparator.comparing(Project::getTotalCost))
                .map(p -> BigDecimal.valueOf(p.getTotalCost()).multiply(BigDecimal.valueOf(0.1)))
                .orElse(BigDecimal.ZERO);
        String highestProject = lossProjects.stream()
                .max(Comparator.comparing(Project::getTotalCost))
                .map(Project::getProjectName)
                .orElse("N/A");

        BigDecimal lowest = lossProjects.stream()
                .min(Comparator.comparing(Project::getTotalCost))
                .map(p ->BigDecimal.valueOf( p.getTotalCost()).multiply(BigDecimal.valueOf(0.1)))
                .orElse(BigDecimal.ZERO);
        String lowestProject = lossProjects.stream()
                .min(Comparator.comparing(Project::getTotalCost))
                .map(Project::getProjectName)
                .orElse("N/A");

        ProjectLossDTO dto = new ProjectLossDTO();
        dto.setTotalLoss(totalLoss);
        dto.setHighestLossAmount(highest);
        dto.setHighestLossProject(highestProject);
        dto.setLowestLossAmount(lowest);
        dto.setLowestLossProject(lowestProject);
        return dto;
    }

    // ===== INJECT ENTITY MANAGER (if not already present) =====
// Add this field at the top of your DashboardServiceImpl
// @PersistenceContext
// private EntityManager entityManager;

    @Override
    public List<TechnologyRevenueDTO> getTechMonthlyRevenue(int year, int month) {
        // 1. Calculate start and end of the given month
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        // 2. Native SQL to group by project_type (technology)
        String sql = "SELECT p.project_type as name, " +
                "       COUNT(p.id) as projects, " +
                "       SUM(p.total_cost) as revenue, " +
                "       SUM(p.total_cost * 0.25) as profit, " +   // Calculate profit as 25% of revenue (ADJUST THIS LOGIC TO MATCH YOUR REAL DATA)
                "       SUM(p.total_cost * 0.05) as loss " +     // Calculate loss as 5% of revenue (ADJUST THIS LOGIC)
                "FROM projects p " +
                "WHERE p.onboarding_date BETWEEN :start AND :end " +
                "GROUP BY p.project_type";

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();

        List<TechnologyRevenueDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            String name = (String) row[0];
            int projects = ((Number) row[1]).intValue();
            double revenue = ((Number) row[2]).doubleValue();
            double profit = ((Number) row[3]).doubleValue();
            double loss = ((Number) row[4]).doubleValue();
            double margin = revenue > 0 ? (profit / revenue) * 100 : 0.0; // Margin % = (Profit / Revenue) * 100

            dtos.add(new TechnologyRevenueDTO(name, projects, revenue, profit, loss, margin));
        }

        return dtos;
    }

    @Override
    public TechnologyRevenueTotalDTO getTechMonthlyTotal(int year, int month) {
        // Simply call the monthly list and sum the totals
        List<TechnologyRevenueDTO> list = getTechMonthlyRevenue(year, month);

        int totalProjects = list.stream().mapToInt(TechnologyRevenueDTO::getProjects).sum();
        double totalRevenue = list.stream().mapToDouble(TechnologyRevenueDTO::getRevenue).sum();
        double totalProfit = list.stream().mapToDouble(TechnologyRevenueDTO::getProfit).sum();
        double totalLoss = list.stream().mapToDouble(TechnologyRevenueDTO::getLoss).sum();
        double avgMargin = list.isEmpty() ? 0.0 : list.stream().mapToDouble(TechnologyRevenueDTO::getMargin).average().orElse(0.0);

        return new TechnologyRevenueTotalDTO(totalProjects, totalRevenue, totalProfit, totalLoss, avgMargin);
    }

    @Override
    public List<TechnologyRevenueDTO> getTechYearlyRevenue(int year) {
        // Calculate start and end of the given year
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        // Native SQL (Same as monthly, but applies to the whole year)
        String sql = "SELECT p.project_type as name, " +
                "       COUNT(p.id) as projects, " +
                "       SUM(p.total_cost) as revenue, " +
                "       SUM(p.total_cost * 0.25) as profit, " +
                "       SUM(p.total_cost * 0.05) as loss " +
                "FROM projects p " +
                "WHERE p.onboarding_date BETWEEN :start AND :end " +
                "GROUP BY p.project_type";

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();

        List<TechnologyRevenueDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            String name = (String) row[0];
            int projects = ((Number) row[1]).intValue();
            double revenue = ((Number) row[2]).doubleValue();
            double profit = ((Number) row[3]).doubleValue();
            double loss = ((Number) row[4]).doubleValue();
            double margin = revenue > 0 ? (profit / revenue) * 100 : 0.0;

            dtos.add(new TechnologyRevenueDTO(name, projects, revenue, profit, loss, margin));
        }

        return dtos;
    }

    @Override
    public TechnologyRevenueTotalDTO getTechYearlyTotal(int year) {
        // Simply call the yearly list and sum the totals
        List<TechnologyRevenueDTO> list = getTechYearlyRevenue(year);

        int totalProjects = list.stream().mapToInt(TechnologyRevenueDTO::getProjects).sum();
        double totalRevenue = list.stream().mapToDouble(TechnologyRevenueDTO::getRevenue).sum();
        double totalProfit = list.stream().mapToDouble(TechnologyRevenueDTO::getProfit).sum();
        double totalLoss = list.stream().mapToDouble(TechnologyRevenueDTO::getLoss).sum();
        double avgMargin = list.isEmpty() ? 0.0 : list.stream().mapToDouble(TechnologyRevenueDTO::getMargin).average().orElse(0.0);

        return new TechnologyRevenueTotalDTO(totalProjects, totalRevenue, totalProfit, totalLoss, avgMargin);
    }

    @Override
    public ProjectOverviewDTO getProjectOverview(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        String sql = "SELECT p.project_type as type, " +
                "       SUM(p.total_cost) as revenue, " +
                "       SUM(p.total_cost * 0.25) as profit, " +  // adjust profit logic if needed
                "       SUM(p.total_cost * 0.05) as loss " +    // adjust loss logic if needed
                "FROM projects p " +
                "WHERE p.onboarding_date BETWEEN :start AND :end " +
                "GROUP BY p.project_type";

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();

        // Initialize default values (0)
        double c2cRevenue = 0, c2cProfit = 0, c2cLoss = 0;
        double c2mRevenue = 0, c2mProfit = 0, c2mLoss = 0;
        double indRevenue = 0, indProfit = 0, indLoss = 0;

        for (Object[] row : results) {
            String type = (String) row[0];
            double revenue = ((Number) row[1]).doubleValue();
            double profit = ((Number) row[2]).doubleValue();
            double loss = ((Number) row[3]).doubleValue();

            if ("C2C".equalsIgnoreCase(type)) {
                c2cRevenue = revenue; c2cProfit = profit; c2cLoss = loss;
            } else if ("C2M".equalsIgnoreCase(type)) {
                c2mRevenue = revenue; c2mProfit = profit; c2mLoss = loss;
            } else if ("INDIVIDUAL".equalsIgnoreCase(type)) {
                indRevenue = revenue; indProfit = profit; indLoss = loss;
            }
        }

        ProjectTypeDTO c2c = new ProjectTypeDTO(c2cRevenue, c2cProfit, c2cLoss);
        ProjectTypeDTO c2m = new ProjectTypeDTO(c2mRevenue, c2mProfit, c2mLoss);
        ProjectTypeDTO individual = new ProjectTypeDTO(indRevenue, indProfit, indLoss);

        return new ProjectOverviewDTO(c2c, c2m, individual);
    }

    private BigDecimal calculateYearRevenue(int year) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);

        List<Project> projects = projectRepository.findProjectsOverlappingYear(startOfYear, endOfYear);
        BigDecimal total = BigDecimal.ZERO;

        for (Project p : projects) {
            LocalDate effStart = p.getOnboardingDate().isBefore(startOfYear) ? startOfYear : p.getOnboardingDate();
            LocalDate effEnd = (p.getEndDate() != null && p.getEndDate().isBefore(endOfYear))
                    ? p.getEndDate() : endOfYear;

            if (effEnd.isBefore(startOfYear) || effStart.isAfter(endOfYear)) continue;

            long totalDays = ChronoUnit.DAYS.between(effStart, effEnd) + 1;
            BigDecimal dailyRate = BigDecimal.valueOf(p.getTotalCost())
                    .divide(BigDecimal.valueOf(totalDays), 4, RoundingMode.HALF_UP);

            total = total.add(dailyRate.multiply(BigDecimal.valueOf(totalDays)));
        }

        return total;
    }

    private BigDecimal calculateMonthRevenue(int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        List<Project> projects = projectRepository.findProjectsOverlappingMonth(startOfMonth, endOfMonth);
        BigDecimal totalRevenue = BigDecimal.ZERO;

        for (Project p : projects) {
            LocalDate effStart = p.getOnboardingDate().isBefore(startOfMonth) ? startOfMonth : p.getOnboardingDate();
            LocalDate effEnd = (p.getEndDate() != null && p.getEndDate().isBefore(endOfMonth))
                    ? p.getEndDate() : endOfMonth;

            if (effEnd.isBefore(startOfMonth) || effStart.isAfter(endOfMonth)) continue;

            long totalDaysProject = ChronoUnit.DAYS.between(
                    p.getOnboardingDate(),
                    (p.getEndDate() != null ? p.getEndDate() : LocalDate.now())
            ) + 1;

            BigDecimal dailyRate = BigDecimal.valueOf(p.getTotalCost())
                    .divide(BigDecimal.valueOf(totalDaysProject), 4, RoundingMode.HALF_UP);

            long daysInMonth = ChronoUnit.DAYS.between(effStart, effEnd) + 1;
            totalRevenue = totalRevenue.add(dailyRate.multiply(BigDecimal.valueOf(daysInMonth)));
        }

        return totalRevenue;
    }

}