package EmployeeManagementSystem.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(){
        return "404";
    }
//    @ExceptionHandler(Exception.class)
//    public String handleException(Exception ex) {
//
//        ex.printStackTrace();
//
//        return "500";
//    }

}
