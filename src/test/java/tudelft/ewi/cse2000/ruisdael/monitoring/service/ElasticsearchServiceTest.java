package tudelft.ewi.cse2000.ruisdael.monitoring.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.elasticsearch.indices.IndicesStatsResponse;
import co.elastic.clients.elasticsearch.indices.stats.IndicesStats;
import co.elastic.clients.util.ObjectBuilder;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tudelft.ewi.cse2000.ruisdael.monitoring.component.DeviceDataConverter;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Device;

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

    @MockBean
    private SearchResponse mockResponse;

    @Autowired
    private ElasticsearchService elasticsearchService;
    
    private static final String MOCK_INDEX_1 = "collector_clone1";
    private static final String MOCK_INDEX_2 = "collector_clone2";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDistinctIndexNames_WithIndices_ReturnsDistinctIndices() throws Exception {
        // Arrange
        when(mockClient.indices()).thenReturn(elasticsearchIndicesClient);
        when(elasticsearchIndicesClient.stats()).thenReturn(indicesStatsResponse);
        when(indicesStatsResponse.indices()).thenReturn(Map.of(MOCK_INDEX_1, indicesStats,
                MOCK_INDEX_2, indicesStats));

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
        when(indicesStatsResponse.indices()).thenReturn(Map.of(MOCK_INDEX_1, indicesStats));
        when(elasticsearchIndicesClient.stats()).thenThrow(IOException.class);

        // Act
        List<String> distinctIndices = elasticsearchService.getDistinctIndexNames();

        ThrowableAssert.ThrowingCallable action = () -> elasticsearchIndicesClient.stats();

        // Assert
        assertTrue(distinctIndices.isEmpty());

        verify(elasticsearchIndicesClient, atLeastOnce()).stats();

        assertThatExceptionOfType(IOException.class).isThrownBy(action);
    }

    @Test
    void getMetricTypes_WithIndices_ReturnsList() throws Exception {
        // Arrange

        // Indices setup
        when(mockClient.indices()).thenReturn(elasticsearchIndicesClient);
        when(elasticsearchIndicesClient.stats()).thenReturn(indicesStatsResponse);
        when(indicesStatsResponse.indices()).thenReturn(Map.of(MOCK_INDEX_1, indicesStats,
                MOCK_INDEX_2, indicesStats));

        // Search request/response setup
        when(mockClient.search((Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                any()))
                .thenReturn(mockResponse);

        // Hits setup
        HitsMetadata mockMetadata = Mockito.mock(HitsMetadata.class);
        Hit mockHit = Mockito.mock(Hit.class);
        when(mockResponse.hits()).thenReturn(mockMetadata);
        when(mockMetadata.hits()).thenReturn(List.of(mockHit));
        when(mockHit.source()).thenReturn(Map.of("a", "a", "b", "b"));

        // Act
        List<String> metrics = elasticsearchService.getMetricTypes();

        // Assert
        assertEquals(3, metrics.size());
        assertTrue(metrics.contains("a"));
        assertTrue(metrics.contains("b"));
        assertTrue(metrics.contains("Status"));

        verify(mockClient, atLeastOnce()).search((Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                any());
        verify(mockResponse, atLeastOnce()).hits();
        verify(mockMetadata, atLeastOnce()).hits();
        verify(mockHit, atLeastOnce()).source();
    }

    @Test
    void getMetricTypes_WithIndices_NoHits_ReturnsEmptyList() throws Exception {
        // Arrange

        // Indices setup
        when(mockClient.indices()).thenReturn(elasticsearchIndicesClient);
        when(elasticsearchIndicesClient.stats()).thenReturn(indicesStatsResponse);
        when(indicesStatsResponse.indices()).thenReturn(Map.of(MOCK_INDEX_1, indicesStats,
                MOCK_INDEX_2, indicesStats));

        // Search request/response setup
        when(mockClient.search((Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                any()))
                .thenReturn(mockResponse);

        // Hits metadata setup
        HitsMetadata mockMetadata = Mockito.mock(HitsMetadata.class);
        when(mockResponse.hits()).thenReturn(mockMetadata);
        when(mockMetadata.hits()).thenReturn(List.of());

        // Act
        List<String> metrics = elasticsearchService.getMetricTypes();

        // Assert
        assertEquals(0, metrics.size());

        verify(mockClient, atLeastOnce()).search((Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                any());
        verify(mockResponse, atLeastOnce()).hits();
        verify(mockMetadata, atLeastOnce()).hits();
    }

    @Test
    void getMetricTypes_WithoutIndices_ReturnsEmptyList() throws Exception {
        // Arrange
        when(mockClient.indices()).thenReturn(elasticsearchIndicesClient);
        when(elasticsearchIndicesClient.stats()).thenReturn(indicesStatsResponse);
        when(indicesStatsResponse.indices()).thenReturn(Map.of());

        when(mockClient.search((Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                any()))
                .thenReturn(mockResponse);

        // Act
        List<String> metrics = elasticsearchService.getMetricTypes();

        // Assert
        assertEquals(0, metrics.size());
        verify(mockClient, never()).search((Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                any());
    }

    @Test
    void getMetricTypes_ThrowsException_ReturnsEmptyList() throws Exception {
        // Arrange
        when(mockClient.indices()).thenReturn(elasticsearchIndicesClient);
        when(elasticsearchIndicesClient.stats()).thenReturn(indicesStatsResponse);
        when(indicesStatsResponse.indices()).thenReturn(Map.of(MOCK_INDEX_1, indicesStats,
                MOCK_INDEX_2, indicesStats));

        when(mockClient.search((Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                any()))
                .thenThrow(IOException.class);

        // Act
        List<String> metrics = elasticsearchService.getMetricTypes();

        ThrowableAssert.ThrowingCallable action = () -> mockClient.search(
                (Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                any());

        // Assert
        assertTrue(metrics.isEmpty());

        verify(mockClient, atLeastOnce()).search((Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                any());

        assertThatExceptionOfType(IOException.class).isThrownBy(action);
    }

    @Test
    void getDeviceDetailsFromName_WithQueryResponse_ReturnsDevice() throws Exception {
        // Arrange
        when(mockClient.search((Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                any()))
                .thenReturn(mockResponse);

        HitsMetadata mockMetadata = Mockito.mock(HitsMetadata.class);
        Hit mockHit1 = Mockito.mock(Hit.class);
        Hit mockHit2 = Mockito.mock(Hit.class);
        when(mockResponse.hits()).thenReturn(mockMetadata);
        when(mockMetadata.hits()).thenReturn(List.of(mockHit1, mockHit2));
        when(mockHit1.source()).thenReturn(Map.of("a", "a", "b", "b"));
        when(mockHit2.source()).thenReturn(Map.of("c", "c", "d", "d"));
        when(mockHit1.index()).thenReturn(MOCK_INDEX_1);

        Device device = new Device();

        try (MockedStatic<DeviceDataConverter> mockConverter = Mockito.mockStatic(DeviceDataConverter.class)) {
            mockConverter.when(() -> DeviceDataConverter.createDeviceFromElasticData("clone1", true,
                            Map.of("a", "a", "b", "b")))
                    .thenReturn(device);

            // Act

            Device resultDevice = elasticsearchService.getDeviceDetailsFromName(MOCK_INDEX_1);

            // Assert
            assertEquals(device, resultDevice);
            verify(mockClient, atLeastOnce()).search((Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                    any());
            verify(mockResponse, atLeastOnce()).hits();
            verify(mockMetadata, atLeastOnce()).hits();
            verify(mockHit1, atLeastOnce()).source();
            verify(mockHit2, never()).source();
            mockConverter.verify(() -> DeviceDataConverter.createDeviceFromElasticData("clone1", true,
                    Map.of("a", "a", "b", "b")));
        }
    }

    @Test
    void getDeviceDetailsFromName_WithQueryResponse_NoHits_ReturnsNull() throws Exception {
        // Arrange
        when(mockClient.search((Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                any()))
                .thenReturn(mockResponse);

        HitsMetadata mockMetadata = Mockito.mock(HitsMetadata.class);
        when(mockResponse.hits()).thenReturn(mockMetadata);
        when(mockMetadata.hits()).thenReturn(List.of());

        // Act
        Device reponseDevice = elasticsearchService.getDeviceDetailsFromName(MOCK_INDEX_1);

        // Assert
        assertNull(reponseDevice);
        verify(mockClient, atLeastOnce()).search((Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                any());
    }

    @Test
    void getDeviceDetailsFromName_ThrowsException_ReturnsNull() throws Exception {
        // Arrange
        when(mockClient.search((Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                any()))
                .thenThrow(IOException.class);

        // Act
        Device reponseDevice = elasticsearchService.getDeviceDetailsFromName(MOCK_INDEX_1);

        ThrowableAssert.ThrowingCallable action = () -> mockClient.search(
                (Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                any());

        // Assert
        assertNull(reponseDevice);
        verify(mockClient, atLeastOnce()).search((Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>) any(),
                any());

        assertThatExceptionOfType(IOException.class).isThrownBy(action);
    }

}