package EmployeeManagementSystem.kafkaConfig;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmployeeConsumer {

    private final JavaMailSender mailSender;

    public EmployeeConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(
            topics = KafkaTopicConfig.EMPLOYEE_CREATED_TOPIC,
            groupId = "employee-email-group"
    )
    public void consumeEmployeeCreatedEvent(EmployeeEvent event) {

        System.out.println(
                "KAFKA CONSUMER: Employee event received"
        );

        System.out.println(event);

        sendWelcomeEmail(event);
    }

    private void sendWelcomeEmail(EmployeeEvent event) {

        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(event.getEmail());

            message.setSubject(
                    "Welcome to Employee Management System"
            );

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

            System.out.println(
                    "MAIL SENT SUCCESSFULLY TO: "
                            + event.getEmail()
            );

        } catch (Exception e) {

            System.err.println(
                    "MAIL SENDING FAILED: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}