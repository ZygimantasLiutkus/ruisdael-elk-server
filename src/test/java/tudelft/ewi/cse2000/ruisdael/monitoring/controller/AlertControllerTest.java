package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Alert;
import tudelft.ewi.cse2000.ruisdael.monitoring.repositories.AlertRepository;

@WebMvcTest(AlertController.class)
public class AlertControllerTest {

    @MockBean
    private AlertRepository alertRepository;

    @InjectMocks
    private AlertController alertController;

    @Autowired
    private MockMvc mockMvc;

    private List<Alert> alerts;
    private Alert alert1 = new Alert(1, "name", "cpu", "0", "1", LocalDateTime.now().minusSeconds(1));
    private Alert alert2 = new Alert(2, "name2", "cpu", "0", "1", LocalDateTime.now());

    /**
     * Setup test environment.
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        alerts = new ArrayList<>();
        alerts.add(alert1);
        alerts.add(alert2);
    }

    @DisplayName("Alerts page")
    @Test
    @WithMockUser(authorities = {"USER"})
    void mainShouldContainData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/alerts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("alerts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("websocketDelay"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("alerts"));
    }

    @DisplayName("Test websocket method")
    @Test
    void checkWebsocketUpdateAlerts() {
        when(alertRepository.findAll()).thenReturn(alerts);

        assertEquals(alert2, alertController.updateAlerts().get(0)); //Checks sorting
    }

    @DisplayName("Test getNodeAlerts helper method")
    @Test
    void checkHelperNodeAlerts() {
        when(alertRepository.findAll()).thenReturn(alerts);

        assertEquals(List.of(alert1), alertController.getNodeAlerts("name"));
    }
}
