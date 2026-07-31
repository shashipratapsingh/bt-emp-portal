//package EmployeeManagementSystem.dto;
//
//import lombok.Data;
//
//@Data
//public class LoginRequest {
//    public String userId;
//    public String password;
//    private String workMode;
//}



package EmployeeManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

//    // Employee/User ID
//    private String userId;
//
//    // Login Password
//    private String password;
//
//    // Browser GPS Location
//    private Double latitude;
//
//    private Double longitude;

    private String userId;
    private String password;
    private String workMode;
    private Double latitude;
    private Double longitude;
}