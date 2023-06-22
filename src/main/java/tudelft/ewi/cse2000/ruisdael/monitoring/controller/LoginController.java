package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller class that controls all endpoints for authentication that are not managed by Spring Security.
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/logout";
    }
}
