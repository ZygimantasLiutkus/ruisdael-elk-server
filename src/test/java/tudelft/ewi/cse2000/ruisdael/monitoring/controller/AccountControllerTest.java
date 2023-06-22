package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.WebSecurityConfig;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.dto.PasswordDTO;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.dto.UserCreationDTO;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.persistent.User;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.persistent.UserRepository;

@SuppressWarnings("PMD.AvoidDuplicateLiterals") //Authority Annotation must be set as constant. Cannot extract for PMD.
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private WebSecurityConfig config;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AccountController accountController;

    private final User demoUser = new User(0L, "demo", "password", true, false, true, false);
    private PasswordDTO passwordDto = new PasswordDTO();
    private UserCreationDTO userCreationDTO = new UserCreationDTO();


    /**
     * Setup test environment.
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        passwordDto = new PasswordDTO("iAmAHash", "test", "test");
        userCreationDTO = new UserCreationDTO("demo", "mypassword", false);
    }

    @DisplayName("Account Management main page: normal user")
    @Test
    @WithMockUser(authorities = {"USER"})
    void mainShouldContainData() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of(demoUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/account-management"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("account-management"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("passworddto"));
    }

    @DisplayName("Account Management main page: admin user")
    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    void mainShouldContainDataAdmin() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of(demoUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/account-management"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("passworddto", "usercreation"))
                .andExpect(MockMvcResultMatchers.model().attribute("users", List.of(demoUser)));
    }

    @DisplayName("Change password: Incorrect existing")
    @Test
    @WithMockUser(authorities = {"USER"})
    void changePasswordIncorrectCurrent() throws Exception {
        when(encoder.matches(any(), anyString())).thenReturn(false);
        when(config.encoder()).thenReturn(encoder);

        mockMvc.perform(MockMvcRequestBuilders.post("/change-password")
                        .flashAttr("passworddto", passwordDto)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/account-management?user_wrong"));
    }

    @DisplayName("Change password: No Match")
    @Test
    @WithMockUser(authorities = {"USER"})
    void changePasswordNoMatch() throws Exception {
        when(encoder.matches(any(), anyString())).thenReturn(true);
        when(config.encoder()).thenReturn(encoder);

        passwordDto.setConfirmPassword("wrongPass");

        mockMvc.perform(MockMvcRequestBuilders.post("/change-password")
                        .flashAttr("passworddto", passwordDto)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/account-management?user_unequal"));
    }

    @DisplayName("Change password: Unknown user")
    @Test
    @WithMockUser(authorities = {"USER"})
    void changePasswordDbError() throws Exception {
        when(encoder.matches(any(), anyString())).thenReturn(true);
        when(encoder.encode(any())).thenReturn("encodedPass");
        when(config.encoder()).thenReturn(encoder);

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/change-password")
                        .flashAttr("passworddto", passwordDto)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/account-management?user_error"));
    }

    @DisplayName("Change password: Success")
    @Test
    @WithMockUser(authorities = {"USER"})
    void changePasswordSuccess() throws Exception {
        when(encoder.matches(any(), anyString())).thenReturn(true);
        when(encoder.encode(any())).thenReturn("encodedPass");
        when(config.encoder()).thenReturn(encoder);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(demoUser));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/change-password")
                        .flashAttr("passworddto", passwordDto)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login?logout"));

        verify(userRepository, atLeastOnce()).saveAndFlush(userCaptor.capture());
        assertEquals("encodedPass", userCaptor.getValue().getPassword());
    }

    @DisplayName("Toggle Admin Status: No Privilege")
    @Test
    @WithMockUser(authorities = {"USER"})
    void toggleAdminNoPrivilege() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin-access/randomuser/true"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @DisplayName("Account Management: Protected Accounts")
    @WithMockUser(authorities = {"USER", "ADMIN"})
    @ParameterizedTest
    @ValueSource(strings = {"/admin-access/admin/true", "/disable-account/admin/true", "/reset-password/admin", "/delete-account/admin"})
    void toggleAdminProtectedAccount(String url) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/account-management?account_adminchange"));
    }

    @DisplayName("Account Management: Own Account")
    @WithMockUser(authorities = {"USER", "ADMIN"})
    @ParameterizedTest
    @ValueSource(strings = {"/admin-access/user/true", "/disable-account/user/true", "/reset-password/user", "/delete-account/user"})
    void toggleAdminOwnAccount(String url) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/account-management?account_ownchange"));
    }

    @DisplayName("Account Management: Unknown user")
    @WithMockUser(authorities = {"USER", "ADMIN"})
    @ParameterizedTest
    @ValueSource(strings = {"/admin-access/username/true", "/disable-account/username/true", "/reset-password/username", "/delete-account/username"})
    void accountManagementUnknownAccount(String url) throws Exception {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/account-management?account_error"));
    }

    @DisplayName("Toggle Admin Status: Guest Account")
    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    void toggleAdminGuestAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin-access/guest/true"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/account-management?account_guestchange"));
    }

    @DisplayName("Toggle Admin: Success")
    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    void toggleAdminSuccess() throws Exception {
        when(userRepository.findByUsername(demoUser.getUsername())).thenReturn(Optional.of(demoUser));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin-access/" + demoUser.getUsername() + "/true"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/account-management"));

        verify(userRepository, atLeastOnce()).saveAndFlush(userCaptor.capture());
        assertTrue(userCaptor.getValue().isAdmin());
    }

    @DisplayName("Toggle Enabled: Success")
    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    void toggleEnabledSuccess() throws Exception {
        when(userRepository.findByUsername(demoUser.getUsername())).thenReturn(Optional.of(demoUser));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/disable-account/" + demoUser.getUsername() + "/false"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/account-management"));

        verify(userRepository, atLeastOnce()).saveAndFlush(userCaptor.capture());
        assertFalse(userCaptor.getValue().isEnabled());
    }

    @DisplayName("Reset Password: Success")
    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    void resetPasswordSuccess() throws Exception {
        when(userRepository.findByUsername(demoUser.getUsername())).thenReturn(Optional.of(demoUser));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(null);

        when(encoder.encode(any())).thenReturn("encodedPass");
        when(config.encoder()).thenReturn(encoder);

        mockMvc.perform(MockMvcRequestBuilders.get("/reset-password/" + demoUser.getUsername()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name(
                        Matchers.containsString("redirect:/account-management?account_pass=")));

        verify(userRepository, atLeastOnce()).saveAndFlush(userCaptor.capture());
        assertEquals("encodedPass", userCaptor.getValue().getPassword());
    }

    @DisplayName("Delete Account: Guest Account")
    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    void deleteGuestAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/delete-account/guest"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/account-management?account_guestchange"));
    }

    @DisplayName("Delete Account: Success")
    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    void deleteAccountSuccess() throws Exception {
        when(userRepository.findByUsername(demoUser.getUsername())).thenReturn(Optional.of(demoUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/delete-account/" + demoUser.getUsername()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/account-management"));

        verify(userRepository, atLeastOnce()).delete(userCaptor.capture());
        assertEquals(demoUser, userCaptor.getValue());
    }

    @DisplayName("Create Account: Exists")
    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    void createAccountExists() throws Exception {
        when(userRepository.findByUsername(demoUser.getUsername())).thenReturn(Optional.of(demoUser));

        mockMvc.perform(MockMvcRequestBuilders.post("/create-account")
                        .flashAttr("usercreationdto", userCreationDTO)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/account-management?create_exists"));

        verify(userRepository, never()).saveAndFlush(any());
    }

    @DisplayName("Create Account: Protected Account Name")
    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    void createAccountProtected() throws Exception {
        userCreationDTO.setUsername("admin");

        mockMvc.perform(MockMvcRequestBuilders.post("/create-account")
                        .flashAttr("usercreationdto", userCreationDTO)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/account-management?account_adminchange"));

        verify(userRepository, never()).saveAndFlush(any());
    }

    @DisplayName("Create Account: Success")
    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    void createAccountSuccess() throws Exception {
        when(userRepository.findByUsername(demoUser.getUsername())).thenReturn(Optional.empty());
        when(encoder.encode(any())).thenReturn("encodedPass");
        when(config.encoder()).thenReturn(encoder);

        mockMvc.perform(MockMvcRequestBuilders.post("/create-account")
                        .flashAttr("usercreationdto", userCreationDTO)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/account-management"));

        verify(userRepository, atLeastOnce()).saveAndFlush(userCaptor.capture());
        assertEquals(userCreationDTO.getUsername(), userCaptor.getValue().getUsername());
        assertEquals(userCreationDTO.isAdmin(), userCaptor.getValue().isAdmin());
    }

    @DisplayName("Check Random Password Method")
    @Test
    void assertPasswordMethod() {
        String generatedPassword = accountController.getRandomPassword();

        //Char should be a-z or A-Z
        for (char ch : generatedPassword.toCharArray()) {
            assertTrue((int) ch >= 97 && (int) ch <= 122);
        }
        assertEquals(8, generatedPassword.toCharArray().length);
        assertNotEquals("", generatedPassword);
    }
}
