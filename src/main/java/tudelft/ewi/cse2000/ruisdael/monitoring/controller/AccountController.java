package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.WebSecurityConfig;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.dto.PasswordDTO;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.dto.UserCreationDTO;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.persistent.User;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.persistent.UserRepository;

/**
 * Controller class that controls all endpoints relating to account management.
 */
@Controller
@SuppressWarnings("PMD.AvoidDuplicateLiterals") //Redirects links.
public class AccountController {

    @Autowired
    private WebSecurityConfig config;

    @Autowired
    private UserRepository userRepository;

    /**
     * Account management page.
     * Allows users to change their own passwords, and admins to create/manage accounts.
     */
    @GetMapping("/account-management")
    public String accountManagementPage(Model model, Authentication authentication) {
        model.addAttribute("passworddto", new PasswordDTO());

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("usercreation", new UserCreationDTO());
        }

        return "account-management";
    }

    /**
     * Allows users to change their own password.
     * Only possible if the current password is also supplied and validated.
     * A user is logged out immediately after changing their password.
     */
    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute(name = "passworddto") PasswordDTO passworddto, Authentication authentication) {
        if (!config.encoder().matches(passworddto.getCurrentPassword(), ((UserDetails) authentication.getPrincipal()).getPassword())) {
            return "redirect:/account-management?user_wrong";
        }

        if (!passworddto.getNewPassword().equals(passworddto.getConfirmPassword())) {
            return "redirect:/account-management?user_unequal";
        }

        try {
            User user = userRepository.findByUsername(authentication.getName()).get();
            user.setPassword(config.encoder().encode(passworddto.getNewPassword()));
            userRepository.saveAndFlush(user);

            return "redirect:/login?logout";
        } catch (Exception e) {
            return "redirect:/account-management?user_error";
        }
    }

    /**
     * Toggles admin access for the specified account.
     * See the {@link #doAccountPreChecks(String, Authentication)} method for preconditions.
     *
     * @return Redirect to the account management page with relevant error information.
     */
    @GetMapping("/admin-access/{username}/{enabled}")
    public String setAdminAccess(@PathVariable("username") String username, @PathVariable("enabled") boolean enabled,
                                           Authentication authentication) {
        String precheck = doAccountPreChecks(username, authentication);

        if (precheck != null) {
            return precheck;
        }

        if (username.equals("guest")) {
            return "redirect:/account-management?account_guestchange";
        }

        try {
            User user = userRepository.findByUsername(username).get();
            user.setAdmin(enabled);
            userRepository.saveAndFlush(user);
        } catch (Exception e) {
            return "redirect:/account-management?account_error";
        }

        return "redirect:/account-management";
    }

    /**
     * Enables/Disables the specified account.
     * See the {@link #doAccountPreChecks(String, Authentication)} method for preconditions.
     *
     * @return Redirect to the account management page with relevant error information.
     */
    @GetMapping("/disable-account/{username}/{enabled}")
    public String setAccountEnabled(@PathVariable("username") String username, @PathVariable("enabled") boolean enabled,
                                           Authentication authentication) {
        String precheck = doAccountPreChecks(username, authentication);

        if (precheck != null) {
            return precheck;
        }

        try {
            User user = userRepository.findByUsername(username).get();
            user.setEnabled(enabled);
            userRepository.saveAndFlush(user);
        } catch (Exception e) {
            return "redirect:/account-management?account_error";
        }

        return "redirect:/account-management";
    }

    /**
     * Resets the password of the specified account.
     * A random password is generated, but should be changed by the user afterward.
     * See the {@link #doAccountPreChecks(String, Authentication)} method for preconditions.
     *
     * @return Redirect to the account management page with relevant error information.
     */
    @GetMapping("/reset-password/{username}")
    public String resetPassword(@PathVariable("username") String username, Authentication authentication) {
        String precheck = doAccountPreChecks(username, authentication);

        if (precheck != null) {
            return precheck;
        }

        try {
            User user = userRepository.findByUsername(username).get();
            String randomPass = getRandomPassword();
            user.setPassword(config.encoder().encode(randomPass));
            userRepository.saveAndFlush(user);
            return "redirect:/account-management?account_pass=" + randomPass;
        } catch (Exception e) {
            return "redirect:/account-management?account_error";
        }
    }

    /**
     * Deletes the specified account.
     * See the {@link #doAccountPreChecks(String, Authentication)} method for preconditions.
     *
     * @return Redirect to the account management page with relevant error information.
     */
    @GetMapping("/delete-account/{username}")
    public String deleteAccount(@PathVariable("username") String username, Authentication authentication) {
        String precheck = doAccountPreChecks(username, authentication);

        if (precheck != null) {
            return precheck;
        }

        if (username.equals("guest")) {
            return "redirect:/account-management?account_guestchange";
        }

        try {
            userRepository.delete(userRepository.findByUsername(username).get());
        } catch (Exception e) {
            return "redirect:/account-management?account_error";
        }

        return "redirect:/account-management";
    }

    /**
     * Create a new user account with the provided information.
     * Only administrators can call this method.
     * @return A redirect to the account management page, where the new account will be shown.
     */
    @PostMapping("/create-account")
    public String createAccount(@ModelAttribute(name = "usercreationdto") UserCreationDTO usercreationdto, Authentication authentication) {
        String precheck = doAccountPreChecks(usercreationdto.getUsername(), authentication);

        if (precheck != null) {
            return precheck;
        }

        try {
            //Assert account doesnt already exist.
            if (userRepository.findByUsername(usercreationdto.getUsername()).isPresent()) {
                return "redirect:/account-management?create_exists";
            }

            User newUser = new User(0L, usercreationdto.getUsername(),
                    config.encoder().encode(usercreationdto.getPassword()),
                    true, true, true, usercreationdto.isAdmin());
            userRepository.saveAndFlush(newUser);

            return "redirect:/account-management";
        } catch (Exception e) {
            return "redirect:/account-management?create_error";
        }
    }

    /**
     * Checks for the following conditions:
     * - A protected account is modified
     * - The users' own account is modified
     * - The user is not an admin
     * If any condition is true, reject the request.
     *
     * @return null on success, a page or exception otherwise
     * @throws ResponseStatusException If unauthorized.
     */
    public String doAccountPreChecks(String username, Authentication authentication) throws ResponseStatusException {
        //Do not allow unauthorized users to change accounts
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        //Protected superuser account
        if (username.equals("admin")) {
            return "redirect:/account-management?account_adminchange";
        }

        if (username.equals(((UserDetails) authentication.getPrincipal()).getUsername())) {
            return "redirect:/account-management?account_ownchange";
        }

        return null;
    }

    /**
     * Returns a random 8 character password.
     */
    public String getRandomPassword() {
        String result = "";

        for (int i = 0; i < 8; i++) {
            //97 is ASCII value for 'a', alphabet contains 26 letters. 122 is the value for 'z'
            result = result.concat(Character.toString(97 + (int) (Math.random() * 25)));
        }

        return result;
    }
}
