package com.example.trendyol.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author created by cengizhan on 3.10.2020
 */
@Configuration
@EnableElasticsearchRepositories
@ComponentScan("com.example.trendyol")
public class AppConfig {

}
