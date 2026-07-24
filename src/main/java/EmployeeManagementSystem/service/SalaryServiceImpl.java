//package EmployeeManagementSystem.service;
//
//import EmployeeManagementSystem.dto.SalarySlipDto;
//import EmployeeManagementSystem.entity.Salary;
//import EmployeeManagementSystem.repository.SalaryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//public class SalaryServiceImpl implements SalaryService {
//
//    @Autowired
//    private SalaryRepository salaryRepository;
//
//    @Override
//    public Salary saveSalary(Salary salary) {
////
//////        double basicSalary =
//////                salary.getBasicSalary() != null ? salary.getBasicSalary() : 0;
////
////        double hra =
////                salary.getHra() != null ? salary.getHra() : 0;
////
//////        double allowance =
//////                salary.getAllowance() != null ? salary.getAllowance() : 0;
////
////        double bonus =
////                salary.getBonus() != null ? salary.getBonus() : 0;
////
////        double deductions =
////                salary.getDeductions() != null ? salary.getDeductions() : 0;
////
//////        double grossSalary =
//////                basicSalary + hra + allowance + bonus;
////
////        double netSalary =
////                grossSalary - deductions;
////
////        salary.setGrossSalary(grossSalary);
////        salary.setNetSalary(netSalary);
////
//        return salaryRepository.save(salary);
//    }
//
//    @Override
//    public Salary getSalaryById(Long id) {
//        return salaryRepository.findById(id).orElse(null);
//    }
//
//    @Override
//    public void deleteSalary(Long id) {
//        salaryRepository.deleteById(id);
//    }
//
//    @Override
//    public Double calculateNetSalary(Long id) {
//
//        Salary salary = salaryRepository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Salary record not found"));
//
////        double basic =
////                salary.getBasicSalary() != null ? salary.getBasicSalary() : 0;
//
//        double hra =
//                salary.getHra() != null ? salary.getHra() : 0;
//
////        double allowance =
////                salary.getAllowance() != null ? salary.getAllowance() : 0;
//
//        double bonus =
//                salary.getBonus() != null ? salary.getBonus() : 0;
//
//        double deductions =
//                salary.getDeductions() != null ? salary.getDeductions() : 0;
//
//        return ( + hra +  + bonus) - deductions;
//    }
//
//    @Override
//    public List<Salary> getAllSalaries() {
//        return salaryRepository.findAll();
//    }
//
//    @Override
//    public SalarySlipDto getSalarySlipById(Long id) {
//
//        Salary salary = salaryRepository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Salary record not found"));
//
//        SalarySlipDto dto = new SalarySlipDto();
////
////        dto.setEmployeeId(salary.getEmployeeId());
////        dto.setEmployeeName(salary.getEmployeeName());
////
////        dto.setBasicSalary(salary.getBasicSalary());
//        dto.setBonus(salary.getBonus());
//        dto.setDeduction(salary.getDeductions());
//
//        dto.setNetSalary(salary.getNetSalary());
//
//        LocalDate today = LocalDate.now();
//
//        dto.setMonthYear(
//                today.getMonth().name() + " " + today.getYear());
//
//        return dto;
//    }
//
//    // ======================
//    // Dashboard Statistics
//    // ======================
//
//    @Override
//    public long getTotalEmployees() {
//        return salaryRepository.count();
//    }
//
//    @Override
//    public long getPaidEmployeesCount() {
//
//        return salaryRepository.findAll()
//                .stream()
//                .filter(s ->
//                        "Paid".equalsIgnoreCase(
//                                s.getPaymentStatus()))
//                .count();
//    }
//
//    @Override
//    public long getPendingEmployeesCount() {
//
//        return salaryRepository.findAll()
//                .stream()
//                .filter(s ->
//                        "Pending".equalsIgnoreCase(
//                                s.getPaymentStatus()))
//                .count();
//    }
//
//    @Override
//    public double getTotalPayrollCost() {
//
//        return salaryRepository.findAll()
//                .stream()
//                .mapToDouble(s ->
//                        s.getNetSalary() != null
//                                ? s.getNetSalary()
//                                : 0)
//                .sum();
//    }
//
//    @Override
//    public double getTotalPaidSalary() {
//
//        return salaryRepository.findAll()
//                .stream()
//                .filter(s ->
//                        "Paid".equalsIgnoreCase(
//                                s.getPaymentStatus()))
//                .mapToDouble(s ->
//                        s.getNetSalary() != null
//                                ? s.getNetSalary()
//                                : 0)
//                .sum();
//    }
//
//    @Override
//    public double getTotalPendingSalary() {
//
//        return salaryRepository.findAll()
//                .stream()
//                .filter(s ->
//                        "Pending".equalsIgnoreCase(
//                                s.getPaymentStatus()))
//                .mapToDouble(s ->
//                        s.getNetSalary() != null
//                                ? s.getNetSalary()
//                                : 0)
//                .sum();
//    }
//}











package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.SalarySlipDto;
import EmployeeManagementSystem.entity.Salary;
import EmployeeManagementSystem.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SalaryServiceImpl implements SalaryService {

    @Autowired
    private SalaryRepository salaryRepository;

    @Override
    public Salary saveSalary(Salary salary) {

        return salaryRepository.save(salary);
    }

    @Override
    public Salary getSalaryById(Long id) {
        return salaryRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteSalary(Long id) {
        salaryRepository.deleteById(id);
    }

    @Override
    public Double calculateNetSalary(Long id) {

        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Salary record not found"));


        double hra =
                salary.getHra() != null ? salary.getHra() : 0;


        double bonus =
                salary.getBonus() != null ? salary.getBonus() : 0;

        double deductions =
                salary.getDeductions() != null ? salary.getDeductions() : 0;

        return ( + hra +  + bonus) - deductions;
    }

    @Override
    public List<Salary> getAllSalaries() {
        return salaryRepository.findAll();
    }

    @Override
    public SalarySlipDto getSalarySlipById(Long id) {

        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Salary record not found"));

        SalarySlipDto dto = new SalarySlipDto();

//        dto.setEmployeeId(salary.getEmployeeId());
//        dto.setEmployeeName(salary.getEmployeeName());
//
//        dto.setBasicSalary(salary.getBasicSalary());
        dto.setBonus(salary.getBonus());
        dto.setDeduction(salary.getDeductions());

        dto.setNetSalary(salary.getNetSalary());

        LocalDate today = LocalDate.now();

        dto.setMonthYear(
                today.getMonth().name() + " " + today.getYear());

        return dto;
    }

    // ======================
    // Dashboard Statistics
    // ======================

    @Override
    public long getTotalEmployees() {
        return salaryRepository.count();
    }

    @Override
    public long getPaidEmployeesCount() {

        return salaryRepository.findAll()
                .stream()
                .filter(s ->
                        "Paid".equalsIgnoreCase(
                                s.getPaymentStatus()))
                .count();
    }

    @Override
    public long getPendingEmployeesCount() {

        return salaryRepository.findAll()
                .stream()
                .filter(s ->
                        "Pending".equalsIgnoreCase(
                                s.getPaymentStatus()))
                .count();
    }

    @Override
    public double getTotalPayrollCost() {

        return salaryRepository.findAll()
                .stream()
                .mapToDouble(s ->
                        s.getNetSalary() != null
                                ? s.getNetSalary()
                                : 0)
                .sum();
    }

    @Override
    public double getTotalPaidSalary() {

        return salaryRepository.findAll()
                .stream()
                .filter(s ->
                        "Paid".equalsIgnoreCase(
                                s.getPaymentStatus()))
                .mapToDouble(s ->
                        s.getNetSalary() != null
                                ? s.getNetSalary()
                                : 0)
                .sum();
    }

    @Override
    public double getTotalPendingSalary() {

        return salaryRepository.findAll()
                .stream()
                .filter(s ->
                        "Pending".equalsIgnoreCase(
                                s.getPaymentStatus()))
                .mapToDouble(s ->
                        s.getNetSalary() != null
                                ? s.getNetSalary()
                                : 0)
                .sum();
    }

    @Override
    public List<Salary> findByEmployeeId(String employeeId) {

        return salaryRepository.findByEmployeeId(employeeId);

    }
}