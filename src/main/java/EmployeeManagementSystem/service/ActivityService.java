package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.Activity;
import org.springframework.data.domain.Page;

public interface ActivityService {

    Page<Activity> getActivities(int page, int size);
}