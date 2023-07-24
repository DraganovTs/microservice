package com.microservices;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class AnalyticsApplication {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}