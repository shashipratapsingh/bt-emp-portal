package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.WfhRequest;

import java.util.List;

public interface WfhService {
    WfhRequest saveRequest(WfhRequest request);
    List<WfhRequest> getWFHEmployees();
}
