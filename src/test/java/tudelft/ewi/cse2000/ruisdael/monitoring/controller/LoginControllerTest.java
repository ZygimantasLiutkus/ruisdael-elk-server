package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private HomeController homeController;

    @DisplayName("Check if logout is returned")
    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    void testLogout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/logout"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @DisplayName("Check if login is returned")
    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    void testLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @DisplayName("Check if nonexistent path returns a 4xx")
    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    void testDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/random"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

}


