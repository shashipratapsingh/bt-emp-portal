package EmployeeManagementSystem.kafkaConfig;

import jdk.jfr.Enabled;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Enabled
public class KafkaTopicConfig {

    public static final String EMPLOYEE_CREATED_TOPIC =
            "employee-created-topic";

    @Bean
    public NewTopic employeeCreatedTopic() {

        return new NewTopic(
                EMPLOYEE_CREATED_TOPIC,
                1,
                (short) 1
        );
    }
}