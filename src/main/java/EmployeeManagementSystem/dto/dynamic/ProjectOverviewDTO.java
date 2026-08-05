package EmployeeManagementSystem.dto.dynamic;

public class ProjectOverviewDTO {
    private ProjectTypeDTO c2c;
    private ProjectTypeDTO c2m;
    private ProjectTypeDTO individual;

    public ProjectOverviewDTO(ProjectTypeDTO c2c, ProjectTypeDTO c2m, ProjectTypeDTO individual) {
        this.c2c = c2c;
        this.c2m = c2m;
        this.individual = individual;
    }

    public ProjectTypeDTO getC2c() { return c2c; }
    public ProjectTypeDTO getC2m() { return c2m; }
    public ProjectTypeDTO getIndividual() { return individual; }
}