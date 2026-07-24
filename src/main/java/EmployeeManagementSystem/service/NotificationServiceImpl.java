package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.entity.Notification;
import EmployeeManagementSystem.repository.EmployeeRepository;
import EmployeeManagementSystem.repository.NotificationRepository;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;
    private  final EmployeeRepository employeeRepository;

    @Override
    public void createNotification(Long employeeId, String message){
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();

        Notification notification = new Notification();

        notification.setEmployee(employee);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        notificationRepository.save(notification);

    }



    public List<Notification>getEmployeeNotification (Long employeeId){
        return notificationRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId);
    }

}
