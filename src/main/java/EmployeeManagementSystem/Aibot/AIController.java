package EmployeeManagementSystem.Aibot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AIController {

    @GetMapping("/welcome-BluethinkChat")
    public String welcomeBluethinkChat() {
        System.out.println("AI Controller Hit");
        return "admin/welcome-BluethinkChat";
    }
}