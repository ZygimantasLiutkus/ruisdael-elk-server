package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.dto.PasswordDTO;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.WebSecurityConfig;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.dto.UserSettingsDTO;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.persistent.User;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.persistent.UserRepository;

@Controller
public class SettingsController {

    @Autowired
    private WebSecurityConfig config;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/settings")
    public String settingsPage(Model model, Authentication authentication) {
        model.addAttribute("passworddto", new PasswordDTO());

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("usersettings", new UserSettingsDTO());
        }

        return "settings";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute PasswordDTO passwordDTO, Authentication authentication) {
        if (!config.encoder().matches(passwordDTO.getCurrentPassword(), ((User) authentication.getPrincipal()).getPassword())) {
            return "redirect:/settings?wrong";
        }

        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            return "redirect:/settings?unequal";
        }

        try {
            User user = userRepository.findByUsername(authentication.getName()).get();
            user.setPassword(config.encoder().encode(passwordDTO.getNewPassword()));
            userRepository.saveAndFlush(user);

            return "redirect:/login?logout";
        } catch(Exception e) {
            return "redirect:/settings?error";
        }
    }

    @PostMapping("/admin-status")
    public String changeAdminStatus() {
        return "redirect:/settings";
    }
}
