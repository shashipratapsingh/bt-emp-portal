package EmployeeManagementSystem.kafkaConfig;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmployeeProducer {

    private final KafkaTemplate<String, EmployeeEvent> kafkaTemplate;

    public EmployeeProducer(
            KafkaTemplate<String, EmployeeEvent> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmployeeCreatedEvent(EmployeeEvent event) {

        kafkaTemplate.send(
                KafkaTopicConfig.EMPLOYEE_CREATED_TOPIC,
                String.valueOf(event.getEmployeeId()),
                event
        ).whenComplete((result, exception) -> {

            if (exception == null) {

                System.out.println(
                        "KAFKA PRODUCER: Employee event sent successfully"
                );

                System.out.println(event);

            } else {

                System.err.println(
                        "KAFKA PRODUCER ERROR: "
                                + exception.getMessage()
                );
            }
        });
    }
}