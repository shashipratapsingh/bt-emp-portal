 package EmployeeManagementSystem.enums;

import lombok.Getter;

@Getter
public enum ProjectStatus {
    ACTIVE("Planned"),
    INACTIVE("Inactive"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    ON_HOLD("On Hold"),
    CANCELLED("Cancelled");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }

}