package com.example.trendyol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrendyolApplication {

    public static void main(String[] args) {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SpringApplication.run(TrendyolApplication.class, args);
    }

}
