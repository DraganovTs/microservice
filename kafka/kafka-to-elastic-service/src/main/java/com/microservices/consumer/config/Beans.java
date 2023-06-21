package com.microservices.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;

@Configuration
public class Beans {


    public KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry(){
        return new KafkaListenerEndpointRegistry();
    }
}
