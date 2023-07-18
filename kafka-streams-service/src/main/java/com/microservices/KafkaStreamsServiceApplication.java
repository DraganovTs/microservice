package com.microservices;

import com.microservices.init.StreamsInitializer;
import com.microservices.runner.StreamsRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaStreamsServiceApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsServiceApplication.class);

    private final StreamsRunner<String, Long> streamsRunner;

    private final StreamsInitializer streamsInitializer;

    public KafkaStreamsServiceApplication(StreamsRunner<String, Long> streamsRunner, StreamsInitializer streamsInitializer) {
        this.streamsRunner = streamsRunner;
        this.streamsInitializer = streamsInitializer;
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("App starts...");
        streamsInitializer.init();
        streamsRunner.start();
    }
}
