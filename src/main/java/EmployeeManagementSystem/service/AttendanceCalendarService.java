package EmployeeManagementSystem.service;

import EmployeeManagementSystem.dto.CalendarEventDTO;

import java.util.List;

public interface AttendanceCalendarService {

    List<CalendarEventDTO> getCalendarEvents(
            String employeeId,
            int year,
            int month
    );
}