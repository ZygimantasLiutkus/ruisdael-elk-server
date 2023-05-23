package nl.tudelft.ewi.gitlab.cse2000.group12b.ruisdaelmonitoringwebui.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DemoController {

    /**
     * Example of how to populate Thymeleaf fields.
     * Manually call this by going to <a href="http://localhost:8080/greeting?fieldname=TU%20Delft">here</a>
     *
     * @param greeting Optional URL Param
     * @param model Thymeleaf Model that allows you to fill the template
     * @return Thymeleaf Template name. The returned string maps to src/main/resources/templates/STRINGHERE
     */
    @GetMapping("/greeting")
    public String thymeleafExample(@RequestParam(name = "fieldname", required = false, defaultValue = "World") String greeting, Model model) {
        model.addAttribute("greeting", new Greeting("Hello, ", greeting));

        return "demo/helloworld";
    }

    /**
     * Post Mapping to handle form submission from Thymeleaf.
     * The ModelAttribute automatically converts the form to the correct Java object and parses values.
     *
     * @param greeting Java Object with populated fields from form submission
     * @param model Thymeleaf Model that allows you to fill the template
     * @return Thymeleaf Template name. The returned string maps to src/main/resources/templates/STRINGHERE
     */
    @PostMapping("/greeting")
    public String thymeleafExamplePost(@ModelAttribute Greeting greeting, Model model) {
        model.addAttribute("greeting", greeting);
        return "demo/helloworld-custom";
    }
}
