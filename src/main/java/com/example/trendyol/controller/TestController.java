package com.example.trendyol.controller;

import com.example.trendyol.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author created by cengizhan on 3.10.2020
 */
@RestController
@RequestMapping("/api")
public class TestController {
    private TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/test")
    public void test(){
        testService.test();
    }
}
