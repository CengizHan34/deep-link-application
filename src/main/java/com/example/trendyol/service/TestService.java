package com.example.trendyol.service;

import com.example.trendyol.model.Test;
import com.example.trendyol.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author created by cengizhan on 3.10.2020
 */
@Service
public class TestService {
    private TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }


    public void test(){
        Test test = new Test();
        test.setId(UUID.randomUUID().toString());
        test.setDeepLink("deeplink");
        test.setWebLink("weblink");
        test.setProcessType("process");
        testRepository.save(test);
    }

}
