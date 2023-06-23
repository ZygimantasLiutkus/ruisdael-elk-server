package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class that redirects the root page to /overview iff they are authenticated.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String homepageView() {
        return "redirect:/overview";
    }
}
