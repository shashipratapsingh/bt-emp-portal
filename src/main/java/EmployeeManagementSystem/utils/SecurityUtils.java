package EmployeeManagementSystem.utils;

import EmployeeManagementSystem.entity.Employee;
import EmployeeManagementSystem.repository.EmployeeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    private final EmployeeRepository employeeRepository;

    public SecurityUtils(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public  Employee getCurrentEmployee(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            throw new RuntimeException("No authentication user found");
        }

        Object principal = authentication.getPrincipal();
        String email;

        if(principal instanceof org.springframework.security.core.userdetails.UserDetails){
            email = ((org.springframework.security.core.userdetails.UserDetails)principal).getUsername();
        }else if(principal instanceof String){
            email = (String)principal;
        }else{
            throw new IllegalStateException("Unexpected Principal type: " +principal.getClass() );
        }
        return employeeRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalStateException("Employee not found with email: " + email));
    }
}
