package EmployeeManagementSystem.kafkaConfig;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeConsumer {
    private final JavaMailSender mailSender;
    //implementing the trying machnism mordern way - step 1
    @RetryableTopic(attempts = "4", backOff = @BackOff(delay = 2000, multiplier = 2.0))
    @KafkaListener(
            topics = KafkaTopicConfig.EMPLOYEE_CREATED_TOPIC,
            groupId = "employee-email-group"
    )
    public void consumeEmployeeCreatedEvent(EmployeeEvent event) {
        System.out.println("KAFKA CONSUMER: Employee event received" + event);
        sendWelcomeEmail(event);
    }
    private void sendWelcomeEmail(EmployeeEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getEmail());
            message.setSubject("Welcome to Employee Management System");
            message.setText(
                    "Hi " + event.getFullName() + ",\n\n" +
                            "Welcome to our organization!\n\n" +
                            "Your employee account has been created successfully.\n\n" +
                            "Employee ID: " + event.getEmployeeId() + "\n" +
                            "Email: " + event.getEmail() + "\n\n" +
                            "Regards,\n" +
                            "HR Team"
            );
            mailSender.send(message);
            System.out.println("MAIL SENT SUCCESSFULLY TO: "+ event.getEmail());
        } catch (Exception e) {
            System.err.println("MAIL SENDING FAILED: "+ e.getMessage());
            // Implemented  retying mechanism in kafka - step 1
            throw new RuntimeException("Email sending failed", e);
        }
    }
}