package EmployeeManagementSystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EmployeeHierarchyDTO {

    private Long id;

    private String name;

    private String designation;

    private List<EmployeeHierarchyDTO> children = new ArrayList<>();

    public EmployeeHierarchyDTO() {
    }

    public EmployeeHierarchyDTO(
            Long id,
            String name,
            String designation
    ) {
        this.id = id;
        this.name = name;
        this.designation = designation;
    }

    public void addChild(EmployeeHierarchyDTO child) {
        this.children.add(child);
    }

    public List<EmployeeHierarchyDTO> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }

        return children;
    }
}