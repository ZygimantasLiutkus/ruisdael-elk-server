package nl.tudelft.ewi.gitlab.cse2000.group12b.ruisdaelmonitoringwebui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homepageView() {
        return "index";
    }
}
