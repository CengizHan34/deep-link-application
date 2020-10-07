package com.example.trendyol.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;

/**
 * @author created by cengizhan on 6.10.2020
 */
@Configuration
@EnableElasticsearchRepositories(queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)
public class ElasticSearchConfig {
    @Autowired
    private Environment environment;

    @Bean
    RestHighLevelClient elasticsearchClient() {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost(environment.getProperty("elasticsearch.cluster-nodes"), 9200, "http")
        ));
        return client;
    }

    @Bean
    ElasticsearchRestTemplate elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
}
