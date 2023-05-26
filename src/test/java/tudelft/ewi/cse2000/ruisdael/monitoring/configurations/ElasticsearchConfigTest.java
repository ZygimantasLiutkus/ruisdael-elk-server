package tudelft.ewi.cse2000.ruisdael.monitoring.configurations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import javax.net.ssl.SSLContext;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
class ElasticsearchConfigTest {

    @MockBean
    private RestClient mockRestClient;
    @MockBean
    private SSLContext mockSslContext;
    @MockBean
    private RestClientBuilder mockRestClientBuilder;
    @Autowired
    private ElasticsearchConfig mockConfig;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(mockConfig, "fingerprint", "fingerprint");
        ReflectionTestUtils.setField(mockConfig, "hostname", "hostname");
        ReflectionTestUtils.setField(mockConfig, "port", 1);
        ReflectionTestUtils.setField(mockConfig, "password", "password");
    }

    @Test
    public void elasticsearchClient_Config_ReturnElasticsearchClient() {
        // Arrange
        when(mockRestClientBuilder.setHttpClientConfigCallback(any())).thenReturn(mockRestClientBuilder);
        when(mockRestClientBuilder.build()).thenReturn(mockRestClient);

        RestClientTransport mockTransport = new RestClientTransport(mockRestClient, new JacksonJsonpMapper());

        // Act
        ElasticsearchClient elasticsearchClient = mockConfig.elasticsearchClient();

        // Assert
        assertNotNull(elasticsearchClient);
    }
}