package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.Activity;
import EmployeeManagementSystem.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public Page<Activity> getActivities(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return activityRepository.findAll(pageable);
    }
}