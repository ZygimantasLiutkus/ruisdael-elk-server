package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homepageView() {
        return "redirect:/overview";
    }
}
