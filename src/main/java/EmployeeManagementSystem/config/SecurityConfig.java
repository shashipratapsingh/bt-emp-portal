package EmployeeManagementSystem.config;

import EmployeeManagementSystem.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * AuthenticationManager Bean
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    /**
     * Password Encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security Configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .formLogin(form -> form.disable())

                .httpBasic(basic -> basic.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico",
                                "/access-denied"
                        ).permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .requestMatchers(
                                "/employee/**",
                                "/employee/profile/**",
                                "/salary/slip/**",
                                "/employee/signoff-logs"
                        ).hasRole("EMPLOYEE")

                        .requestMatchers(
                                "/leave/manage",
                                "/leave/status/**",
                                "/timesheet/manage",
                                "/timesheet/status/**",
                                "/manager/profile"
                        ).hasRole("MANAGER")

                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint((request, response, ex) ->
                                response.sendRedirect("/auth/loginPage?expired=true")
                        )

                        .accessDeniedHandler((request, response, ex) ->
                                response.sendRedirect("/auth/access-denied")
                        )
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    /**
     * Custom Access Denied Handler
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {

        return (request, response, accessDeniedException) -> {

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");

            response.getWriter()
                    .write("{\"error\":\"Access Denied\"}");
        };
    }
}