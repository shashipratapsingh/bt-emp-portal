package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.Notification;

import java.util.List;

public interface NotificationService {
    void createNotification (Long employeeId, String message);

     List<Notification> getEmployeeNotification(Long employeeId);
}
