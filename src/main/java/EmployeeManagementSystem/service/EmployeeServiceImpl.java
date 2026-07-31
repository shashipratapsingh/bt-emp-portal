package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.AnniversaryDTO;
import EmployeeManagementSystem.dto.BirthdayDTO;
import EmployeeManagementSystem.dto.CelebrationDto;
import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.Salary;
import EmployeeManagementSystem.kafkaConfig.EmployeeEvent;
import EmployeeManagementSystem.kafkaConfig.EmployeeProducer;
import EmployeeManagementSystem.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {


    @Autowired
    private final EmployeeRepository employeeRepository;
    private final EmployeeProducer employeeProducer;



    // =========================
    // SAVE EMPLOYEE
    // =========================
    private final EmployeeProfileRepository employeeProfileRepository;

    @Override
    public Employee saveEmployee(Employee employee) {


        // Salary mapping
        if (employee.getSalaries() != null) {

            employee.getSalaries()
                    .forEach(salary ->
                            salary.setEmployee(employee)
                    );

            if (employee.getSalaries() != null) {
                employee.getSalaries().forEach(salary -> salary.setEmployee(employee));
            }


            // Attendance mapping
            if (employee.getAttendanceList() != null) {
                employee.getAttendanceList()
                        .forEach(a -> a.setEmployee(employee));
            }

            // Save employee
            Employee savedEmployee =
                    employeeRepository.save(employee);

            // Prepare employee name
            String employeeName = savedEmployee.getFullName();

            if (employeeName == null || employeeName.isBlank()) {

                employeeName =
                        savedEmployee.getFirstName()
                                + " "
                                + savedEmployee.getLastName();

                employee.getAttendanceList()
                        .forEach(attendance ->
                                attendance.setEmployee(employee)
                        );
            }


            // Create Kafka Event
            EmployeeEvent employeeEvent = new EmployeeEvent(savedEmployee.getId(), employeeName, savedEmployee.getEmail());

            // Publish event
            employeeProducer.sendEmployeeCreatedEvent(employeeEvent);

            return savedEmployee;
        }

        return employee;
    }


        // =========================
        // FIND BY DEPARTMENT
        // =========================

        @Override
        public List<Employee> getEmployeesByDepartment (Long departmentId){

            return employeeRepository.findByDepartmentId(departmentId);
        }


        // =========================
        // FIND ALL
        // =========================

        @Override
        public Object findAll() {

            return employeeRepository.findAll();
        }


        // =========================
        // UPDATE EMPLOYEE
        // =========================

        @Override
        public Employee updateEmployee (Long id, Employee employee){


            Employee existing = employeeRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Employee not found"));


            existing.setFirstName(employee.getFirstName());
            existing.setLastName(employee.getLastName());
            existing.setEmail(employee.getEmail());
            existing.setPhone(employee.getPhone());
            existing.setJoiningDate(employee.getJoiningDate());
            existing.setDepartment(employee.getDepartment());


            // Update Salary List

            if (employee.getSalaries() != null) {


                existing.getSalaries().clear();


                employee.getSalaries()
                        .forEach(salary -> {

                            salary.setEmployee(existing);

                            existing.getSalaries()
                                    .add(salary);

                        });
            }


            // Update Attendance List

            if (employee.getAttendanceList() != null) {


                existing.getAttendanceList().clear();


                employee.getAttendanceList()
                        .forEach(attendance -> {

                            attendance.setEmployee(existing);

                            existing.getAttendanceList()
                                    .add(attendance);

                        });

            }


            return employeeRepository.save(existing);
        }


        @Override
        public void deleteEmployee (Long id){


            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Employee not found"));


            employeeRepository.delete(employee);

        }


        // =========================
        // GET EMPLOYEE BY ID
        // =========================

        @Override
        public Employee getEmployeeById(Long id){


            return employeeRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Employee not found"));

        }


        // =========================
        // ALL EMPLOYEES LIST
        // =========================

        @Override
        public List<Employee> getAllEmployeesList(){

            return Collections.singletonList((Employee) employeeRepository.findAll());

        }


        // =========================
        // PAGINATION
        // =========================

        @Override
        public Page<Employee> getAllEmployees(
        int pageNo,
        int pageSize,
        String sortBy){


            Pageable pageable = PageRequest.of(
                    pageNo,
                    pageSize,
                    Sort.by(
                            sortBy == null || sortBy.isBlank()
                                    ? "id"
                                    : sortBy
                    ).ascending()
            );


            return employeeRepository.findAll(pageable);

        }


        // =========================
        // SEARCH EMPLOYEE
        // =========================

        @Override
        public Page<Employee> searchEmployee (
                String keyword,
                Pageable pageable){


            return employeeRepository.searchAll(
                    keyword,
                    pageable
            );

        }


        // =========================
        // FIND BY EMAIL
        // =========================

        @Override
        public Employee findByEmail (String email){


            return employeeRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("Employee not found"));

        }


        @Override
        public long totalEmployees () {

            return employeeRepository.count();

        }


        @Override
        public List<Employee> getAllEmployees () {

            return employeeRepository.findAll();

        }


        @Override
        public List<Employee> getEmployeesByIds (List < Long > ids) {


            return employeeRepository.findAllById(ids);

        }


        // =========================
        // UPCOMING BIRTHDAYS
        // =========================

        @Override
        public List<BirthdayDTO> getUpcomingBirthdays() {


            LocalDate today = LocalDate.now();

        return employeeProfileRepository.findAll()
                .stream()
                .filter(emp -> emp.getDob() != null)
                .map(emp -> {
                    LocalDate nextBirthday = emp.getDob().withYear(today.getYear());

                    if (nextBirthday.isBefore(today)) {
                        nextBirthday = nextBirthday.plusYears(1);
                    }

                    long days = ChronoUnit.DAYS.between(today, nextBirthday);

            return employeeRepository.findAll()
                    .stream()
                    .filter(emp ->
                            emp.getDateOfBirth() != null
                    )
                    .map(emp -> {


                        LocalDate nextBirthday =
                                emp.getDateOfBirth()
                                        .withYear(today.getYear());


                        if (nextBirthday.isBefore(today)) {

                            nextBirthday =
                                    nextBirthday.plusYears(1);

                        }


                    BirthdayDTO dto = new BirthdayDTO();
                    dto.setName(emp.getFullName());          // fullName use karo
                    dto.setDob(emp.getDob());                // getDob() use karo
                    dto.setDepartment(emp.getDepartment());  // department String hai
                    dto.setRemainingDays(days);
                    dto.setNextDate(nextBirthday);

                    return dto;
                })
                .sorted(Comparator.comparingLong(BirthdayDTO::getRemainingDays))
                .limit(5)
                .toList();
    }
                        BirthdayDTO dto = new BirthdayDTO();


                        dto.setName(emp.getFirstName());
                        dto.setDateOfBirth(emp.getDateOfBirth());

                        dto.setRemainingDays(
                                ChronoUnit.DAYS.between(
                                        today,
                                        nextBirthday
                                )
                        );

                        dto.setNextDate(nextBirthday);


                        return dto;


                    })
                    .sorted(
                            Comparator.comparingLong(
                                    BirthdayDTO::getRemainingDays
                            )
                    )
                    .limit(5)
                    .toList();

        }


        // =========================
        // UPCOMING ANNIVERSARIES
        // =========================

        @Override
        public List<AnniversaryDTO> getUpcomingAnniversaries () {


            LocalDate today = LocalDate.now();

        return employeeProfileRepository.findAll()
                .stream()
                .filter(emp -> emp.getRegisteredAt() != null)
                .map(emp -> {
                    LocalDate nextAnniversary = LocalDate.from(emp.getRegisteredAt().withYear(today.getYear()));
                    if (nextAnniversary.isBefore(today)) {
                        nextAnniversary = nextAnniversary.plusYears(1);
                    }
                    long days = ChronoUnit.DAYS.between(today, nextAnniversary);

            return employeeRepository.findAll()
                    .stream()
                    .filter(emp ->
                            emp.getJoiningDate() != null
                    )
                    .map(emp -> {


                        LocalDate nextAnniversary =
                                emp.getJoiningDate()
                                        .withYear(today.getYear());


                        if (nextAnniversary.isBefore(today)) {

                            nextAnniversary =
                                    nextAnniversary.plusYears(1);

                        }

                    AnniversaryDTO dto = new AnniversaryDTO();
                    dto.setName(emp.getFullName());
                    dto.setJoiningDate(LocalDate.from(emp.getRegisteredAt()));
                    dto.setRemainingDays(days);
                    dto.setNextDate(nextAnniversary);
                    return dto;
                })
                .sorted(Comparator.comparingLong(AnniversaryDTO::getRemainingDays))
                .limit(5)
                .toList();
    }

                        AnniversaryDTO dto =
                                new AnniversaryDTO();


                        dto.setName(emp.getFirstName());

                        dto.setJoiningDate(
                                emp.getJoiningDate()
                        );


                        dto.setRemainingDays(
                                ChronoUnit.DAYS.between(
                                        today,
                                        nextAnniversary
                                )
                        );


                        dto.setNextDate(nextAnniversary);


                        return dto;


                    })
                    .sorted(
                            Comparator.comparingLong(
                                    AnniversaryDTO::getRemainingDays
                            )
                    )
                    .limit(5)
                    .toList();

        }

    }

//    @Override
//    public Employee updateEmployee(Long id, Employee employee) {
//        return null;
//    }
//
//    @Override
//    public void deleteEmployee(Long id) {
//
//    }
//
//    @Override
//    public Employee getEmployeeById(Long id) {
//        return null;
//    }
//
//    @Override
//    public Page<Employee> getAllEmployees(int pageNo, int pageSize, String sortBy) {
//        return null;
//    }
//
//    @Override
//    public Page<Employee> searchEmployee(String keyword, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public List<Employee> getAllEmployeesList() {
//        return List.of();
//    }
//
//    @Override
//    public Employee findByEmail(String email) {
//        return null;
//    }
//
//    @Override
//    public long totalEmployees() {
//        return 0;
//    }
//
//    @Override
//    public List<BirthdayDTO> getUpcomingBirthdays() {
//        return List.of();
//    }
//
//    @Override
//    public List<AnniversaryDTO> getUpcomingAnniversaries() {
//        return List.of();
//    }
//
//    @Override
//    public List<Employee> getAllEmployees() {
//        return List.of();
//    }
//
//    @Override
//    public List<Employee> getEmployeesByIds(List<Long> ids) {
//        return List.of();
//    }
//
//    @Override
//    public List<Employee> getEmployeesByDepartment(Long departmentId) {
//        return List.of();
//    }
//
//    @Override
//    public @Nullable Object findAll() {
//        return null;
//    }
