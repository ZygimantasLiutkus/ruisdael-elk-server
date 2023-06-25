package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Check if login is returned")
    @Test
    void testLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @DisplayName("Check if login pages return the correct views")
    @Test
    void testPageViews() {
        assertEquals("login", loginController.loginPage());
        assertEquals("redirect:/logout", loginController.logout());
    }
}


