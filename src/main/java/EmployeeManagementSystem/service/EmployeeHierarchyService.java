package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.EmployeeHierarchyDTO;
import EmployeeManagementSystem.entity.RegisterEmployee;
import EmployeeManagementSystem.repository.RegisterEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeHierarchyService {

    private final RegisterEmployeeRepository registerEmployeeRepository;


    public List<EmployeeHierarchyDTO> getEmployeeHierarchy() {

        List<RegisterEmployee> employees =
                registerEmployeeRepository.findAll();

        if (employees == null || employees.isEmpty()) {
            return new ArrayList<>();
        }


        /*
         * ============================================================
         * STEP 1
         * Create DTO for every employee
         * ============================================================
         */

        Map<Long, EmployeeHierarchyDTO> employeeMap =
                new HashMap<>();

        for (RegisterEmployee employee : employees) {

            if (employee.getId() == null) {
                continue;
            }

            EmployeeHierarchyDTO dto =
                    new EmployeeHierarchyDTO(
                            employee.getId(),
                            employee.getName(),
                            employee.getDesignation()
                    );

            employeeMap.put(
                    employee.getId(),
                    dto
            );
        }


        /*
         * ============================================================
         * STEP 2
         * Build hierarchy
         * ============================================================
         */

        List<EmployeeHierarchyDTO> roots =
                new ArrayList<>();


        for (RegisterEmployee employee : employees) {

            if (employee.getId() == null) {
                continue;
            }

            EmployeeHierarchyDTO currentEmployee =
                    employeeMap.get(employee.getId());

            Long managerId =
                    employee.getReportingManagerId();


            /*
             * No reporting manager
             * ===================
             * This employee becomes ROOT.
             */

            if (managerId == null) {

                roots.add(currentEmployee);

                continue;
            }


            /*
             * Find reporting manager
             */

            EmployeeHierarchyDTO manager =
                    employeeMap.get(managerId);


            /*
             * Manager exists
             * ==============
             * Add employee under manager.
             */

            if (manager != null) {

                manager.addChild(
                        currentEmployee
                );

            } else {

                /*
                 * Manager ID exists in database
                 * but manager record was not found.
                 *
                 * Treat employee as root so that
                 * employee does not disappear.
                 */

                roots.add(currentEmployee);
            }
        }

        return roots;
    }
}