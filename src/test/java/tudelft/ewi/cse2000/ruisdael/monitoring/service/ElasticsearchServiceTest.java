package tudelft.ewi.cse2000.ruisdael.monitoring.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.elasticsearch.indices.IndicesStatsResponse;
import co.elastic.clients.elasticsearch.indices.stats.IndicesStats;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ElasticsearchServiceTest {

    @MockBean
    private ElasticsearchClient mockClient;

    @MockBean
    private ElasticsearchIndicesClient elasticsearchIndicesClient;

    @MockBean
    private IndicesStatsResponse indicesStatsResponse;

    @MockBean
    private IndicesStats indicesStats;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDistinctIndexNames_WithIndices_ReturnsDistinctIndices() throws Exception {
        // Arrange
        when(mockClient.indices()).thenReturn(elasticsearchIndicesClient);
        when(elasticsearchIndicesClient.stats()).thenReturn(indicesStatsResponse);
        when(indicesStatsResponse.indices()).thenReturn(Map.of("collector_clone1", indicesStats,
                "collector_clone2", indicesStats));

        // Act
        List<String> distinctIndices = elasticsearchService.getDistinctIndexNames();

        // Assert
        assertEquals(2, distinctIndices.size());
        assertTrue(distinctIndices.contains("clone1"));
        assertTrue(distinctIndices.contains("clone2"));

        verify(indicesStatsResponse, atLeastOnce()).indices();
    }

    @Test
    void getDistinctIndexNames_WithoutIndices_ReturnsEmptyList() throws Exception {
        // Arrange
        when(mockClient.indices()).thenReturn(elasticsearchIndicesClient);
        when(elasticsearchIndicesClient.stats()).thenReturn(indicesStatsResponse);
        when(indicesStatsResponse.indices()).thenReturn(Map.of());

        // Act
        List<String> distinctIndices = elasticsearchService.getDistinctIndexNames();

        // Assert
        assertTrue(distinctIndices.isEmpty());

        verify(indicesStatsResponse, atLeastOnce()).indices();
    }

    @Test
    void getDistinctIndexNames_WithException_ReturnsEmptyList() throws Exception {
        // Arrange
        when(mockClient.indices()).thenReturn(elasticsearchIndicesClient);
        when(elasticsearchIndicesClient.stats()).thenReturn(indicesStatsResponse);
        when(indicesStatsResponse.indices()).thenReturn(Map.of("collector_clone1", indicesStats));
        when(elasticsearchIndicesClient.stats()).thenThrow(IOException.class);

        // Act
        List<String> distinctIndices = elasticsearchService.getDistinctIndexNames();

        ThrowableAssert.ThrowingCallable action = () -> elasticsearchIndicesClient.stats();

        // Assert
        assertTrue(distinctIndices.isEmpty());

        verify(elasticsearchIndicesClient, atLeastOnce()).stats();

        assertThatExceptionOfType(IOException.class).isThrownBy(action);
    }
}