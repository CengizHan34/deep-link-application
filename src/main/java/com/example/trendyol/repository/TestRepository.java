package com.example.trendyol.repository;

import com.example.trendyol.model.Test;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author created by cengizhan on 3.10.2020
 */
@Repository
public interface TestRepository extends ElasticsearchRepository<Test,String> {
}
