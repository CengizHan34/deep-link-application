package com.example.trendyol.repository;

import com.example.trendyol.model.Link;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author created by cengizhan on 3.10.2020
 */
public interface LinkRepository extends ElasticsearchRepository<Link, String> {
}
