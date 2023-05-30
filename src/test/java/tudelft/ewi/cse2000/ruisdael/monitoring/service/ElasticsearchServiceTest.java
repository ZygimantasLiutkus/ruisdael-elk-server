package tudelft.ewi.cse2000.ruisdael.monitoring.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
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
    private SearchResponse mockResponse;
    @MockBean
    private HitsMetadata mockHitMeta;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDistinctIndexNames_WithIndices_ReturnsDistinctIndices() throws Exception {
        // Arrange
        when(mockHitMeta.hits()).thenReturn(List.of(
                new Hit.Builder().id("A").index("metric_clone1").build(),
                new Hit.Builder().id("B").index("metric_clone2").build()
        ));

        when(mockResponse.hits()).thenReturn(mockHitMeta);

        when(mockClient.search(any(Function.class), any())).thenReturn(mockResponse);

        // Act
        List<String> distinctIndices = elasticsearchService.getDistinctIndexNames();

        // Assert
        assertEquals(2, distinctIndices.size());
        assertTrue(distinctIndices.contains("metric_clone1"));
        assertTrue(distinctIndices.contains("metric_clone2"));

        verify(mockClient).search(any(Function.class), any());
    }

    @Test
    void getDistinctIndexNames_WithoutIndices_ReturnsEmptyList() throws Exception {
        // Arrange
        when(mockHitMeta.hits()).thenReturn(List.of());

        when(mockResponse.hits()).thenReturn(mockHitMeta);

        when(mockClient.search(any(Function.class), any())).thenReturn(mockResponse);

        // Act
        List<String> distinctIndices = elasticsearchService.getDistinctIndexNames();

        // Assert
        assertTrue(distinctIndices.isEmpty());

        verify(mockClient).search(any(Function.class), any());
    }

    @Test
    void getDistinctIndexNames_WithException_ReturnsEmptyList() throws Exception {
        // Arrange
        when(mockClient.search(any(Function.class), any())).thenThrow(IOException.class);

        // Act
        List<String> distinctIndices = elasticsearchService.getDistinctIndexNames();

        ThrowableAssert.ThrowingCallable action = () -> mockClient.search(q -> q, Integer.class);

        // Assert
        assertTrue(distinctIndices.isEmpty());

        verify(mockClient).search(any(Function.class), any());

        assertThatExceptionOfType(IOException.class).isThrownBy(action);
    }
}