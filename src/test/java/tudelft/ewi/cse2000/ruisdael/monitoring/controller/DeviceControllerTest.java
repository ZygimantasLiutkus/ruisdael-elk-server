package tudelft.ewi.cse2000.ruisdael.monitoring.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.elastic.clients.elasticsearch._types.AcknowledgedResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
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
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Bandwidth;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Device;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Instrument;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Location;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Ram;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.Storage;
import tudelft.ewi.cse2000.ruisdael.monitoring.device.enums.Status;
import tudelft.ewi.cse2000.ruisdael.monitoring.repositories.IndexRepository;
import tudelft.ewi.cse2000.ruisdael.monitoring.service.ElasticsearchService;

@SuppressWarnings("PMD.AvoidDuplicateLiterals") //Authority Annotation must be set as constant. Cannot extract for PMD.
@WebMvcTest(DeviceController.class)
class DeviceControllerTest {

    @MockBean
    private ElasticsearchService elasticsearchService;

    @MockBean
    private IndexRepository indexRepository;

    @MockBean
    private AlertController alertController;

    @InjectMocks
    private DeviceController deviceController;

    @Autowired
    private MockMvc mockMvc;

    private static final Device dummyDevice = new Device("Dummy", new Instrument("in", "it"),
            new Location(1.0, 2.0, "le", "ln"), Status.ONLINE,
            new Storage(0.0, 0.0, 0.0, 0.0),
            new Ram(0.0, 0.0, 0.0, 0.0, 0.0),
            1.0, new Bandwidth(0.0, 0.0, 0.0, 0.0),
            "t", Map.of());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testGetOverview() throws Exception {
        when(elasticsearchService.getAllDevices()).thenReturn(List.of(dummyDevice));

        mockMvc.perform(MockMvcRequestBuilders.get("/overview"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("websocketDelay", "metrics"))
                .andExpect(MockMvcResultMatchers.model().attribute("devices", List.of(dummyDevice)));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testGetNodePage_LastHitNull() throws Exception {
        when(elasticsearchService.getDeviceDetailsFromName("node-index")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/node/{node_index}", "node-index"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("node"))
                .andExpect(MockMvcResultMatchers.model().attribute("noData", true));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testGetNodePage() throws Exception {
        when(elasticsearchService.getDeviceDetailsFromName("node-index")).thenReturn(dummyDevice);

        mockMvc.perform(MockMvcRequestBuilders.get("/node/{node_index}", "node-index"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("node"))
                .andExpect(MockMvcResultMatchers.model().attribute("device", dummyDevice));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testDeviceList() throws Exception {
        when(elasticsearchService.getAllDevices()).thenReturn(List.of(dummyDevice));

        mockMvc.perform(MockMvcRequestBuilders.get("/device-list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("device-list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("metrics"))
                .andExpect(MockMvcResultMatchers.model().attribute("devices", List.of(dummyDevice)));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testUpdateDevices() throws Exception {
        when(elasticsearchService.getAllDevices()).thenReturn(List.of(dummyDevice));
        ObjectWriter toJson = new ObjectMapper().writer();
        mockMvc.perform(MockMvcRequestBuilders.get("/device-update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(toJson.writeValueAsString(List.of(dummyDevice))));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testDeleteIndexTrue() throws Exception {
        AcknowledgedResponse response = mock(AcknowledgedResponse.class);
        when(response.acknowledged()).thenReturn(true);
        when(elasticsearchService.deleteIndex(any())).thenReturn(response);
        when(elasticsearchService.deleteIndex("dummyIndex").acknowledged()).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/delete/dummyIndex"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testDeleteIndexFalse() throws Exception {
        AcknowledgedResponse response = mock(AcknowledgedResponse.class);
        when(response.acknowledged()).thenReturn(false);
        when(elasticsearchService.deleteIndex(any())).thenReturn(response);
        when(elasticsearchService.deleteIndex("dummyIndex").acknowledged()).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/delete/dummyIndex"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testDisableIndexExists() throws Exception {
        when(indexRepository.existsByIndexValue(anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/disable/any"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testDisableNonExistent() throws Exception {
        when(indexRepository.existsByIndexValue(anyString())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/disable/any"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testEnableIndexExists() throws Exception {
        when(indexRepository.existsByIndexValue(anyString())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/enable/any"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testEnableIndexNonExistent() throws Exception {
        when(indexRepository.existsByIndexValue(anyString())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/enable/any"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testEnableIndexExistent() throws Exception {
        when(indexRepository.existsByIndexValue(anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/enable/any"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

}
