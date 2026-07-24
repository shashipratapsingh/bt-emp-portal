package EmployeeManagementSystem.config;

import EmployeeManagementSystem.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupRunner implements ApplicationRunner {
    private final UserSessionRepository repository;

    @Override
    public void run(ApplicationArguments args) {

        repository.deactivateAllSessions();

        System.out.println("All user sessions have been invalidated.");
    }
}
