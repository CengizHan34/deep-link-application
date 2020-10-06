package com.example.trendyol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
@ComponentScan("com.example.trendyol")
@SpringBootApplication
public class TrendyolApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrendyolApplication.class, args);
    }

}
