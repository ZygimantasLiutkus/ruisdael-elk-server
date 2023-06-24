package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Check if overview is returned")
    @Test
    @WithMockUser(authorities = {"USER"})
    void testHomePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/overview"));
    }

    @DisplayName("Check if nonexistent path returns a 4xx")
    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    void testDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/random"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
