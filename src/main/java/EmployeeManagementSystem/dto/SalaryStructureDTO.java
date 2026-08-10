// SalaryStructureDTO.java
package EmployeeManagementSystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryStructureDTO {
 private Long id;
 private Long employeeProfileId;  // Changed from employeeId to employeeProfileId
 private BigDecimal basicSalary = BigDecimal.ZERO;
 private BigDecimal hra = BigDecimal.ZERO;
 private BigDecimal conveyance = BigDecimal.ZERO;
 private BigDecimal medicalAllowance = BigDecimal.ZERO;
 private BigDecimal specialAllowance = BigDecimal.ZERO;
 private BigDecimal otherAllowance = BigDecimal.ZERO;
 private BigDecimal pf = BigDecimal.ZERO;
 private BigDecimal esi = BigDecimal.ZERO;
 private BigDecimal professionalTax = BigDecimal.ZERO;
 private BigDecimal tds = BigDecimal.ZERO;
 private BigDecimal loanDeduction = BigDecimal.ZERO;
 private LocalDate effectiveFrom;
}