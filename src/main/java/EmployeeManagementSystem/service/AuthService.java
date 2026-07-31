package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.RegisterEmployee;
import EmployeeManagementSystem.jwt.JwtUtil;
import EmployeeManagementSystem.repository.RegisterEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final GeofenceService geofenceService;
    private final JwtUtil jwtUtil;
    private final RegisterEmployeeRepository registerEmployeeRepository;

    public String login(String userId,
                        String password,
                        Double latitude,
                        Double longitude) {

        // Validate Location
        if (latitude == null || longitude == null) {
            throw new RuntimeException("Location is required for Work From Office login.");
        }

        // Authenticate Username & Password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userId,
                        password
                )
        );

        // Validate Office Geofence
        if (!geofenceService.isInsideOffice(latitude, longitude)) {
            throw new RuntimeException(
                    "You are outside the office area. Login is not allowed."
            );
        }

        // Fetch Employee Details
        RegisterEmployee employee = registerEmployeeRepository.findByUserId(userId);

        if (employee == null) {
            throw new RuntimeException("Employee not found.");
        }

        // Generate JWT Token
        return jwtUtil.generateToken(
                employee.getUserId(),
                employee.getRole()
        );
    }
}