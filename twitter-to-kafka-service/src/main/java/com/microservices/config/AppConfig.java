package com.microservices.config;

import com.microservices.listener.TwitterKafkaStatusListener;
import com.microservices.runner.StreamRunner;
import com.microservices.runner.impl.MockKafkaStreamRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public StreamRunner streamRunner(TwitterToKafkaServiceConfigData configData, TwitterKafkaStatusListener statusListener) {
        return new MockKafkaStreamRunner(configData, statusListener);
    }
}