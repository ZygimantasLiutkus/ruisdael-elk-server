package tudelft.ewi.cse2000.ruisdael.monitoring.configurations;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Elasticsearch client.
 */
@Configuration
public class ElasticsearchConfig {

    @Value("${ca.cert}")
    private String fingerprint;
    @Value("${elasticsearch.host.address}")
    private String hostname;
    @Value("${elasticsearch.host.port}")
    private int port;
    @Value("${elastic.user.password}")
    private String password;

    /**
     * Creates and configures the Elasticsearch client.
     *
     * @return The configured Elasticsearch client.
     */
    @Bean
    public ElasticsearchClient elasticsearchClient() {

        // Create SSLContext from CA fingerprint
        SSLContext sslContext = TransportUtils
                .sslContextFromCaFingerprint(this.fingerprint);

        // Configure credentials for authentication
        CredentialsProvider cp = new BasicCredentialsProvider();
        cp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", this.password));

        // Build and configure the RestClient
        RestClient restClient = RestClient
                .builder(new HttpHost(this.hostname, this.port, "https"))
                .setHttpClientConfigCallback(hc -> hc
                        .setSSLContext(sslContext)
                        .setDefaultCredentialsProvider(cp)
                )
                .build();

        // Create ElasticsearchTransport using RestClient and JSON mapper
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        // Create and return the ElasticsearchClient
        return new ElasticsearchClient(transport);
    }
}
