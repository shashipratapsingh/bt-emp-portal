package EmployeeManagementSystem.kafkaConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {
     // Implemented  retying mechanism in kafka - step 2  (this is class way to implemened) for mordern
    //I have configured in consure class.
    @Bean
    public DefaultErrorHandler errorHandler() {
        FixedBackOff fixedBackOff =
                new FixedBackOff(5000L, 3L);  // 5 sec will try again
        return new DefaultErrorHandler(fixedBackOff);
    }
}