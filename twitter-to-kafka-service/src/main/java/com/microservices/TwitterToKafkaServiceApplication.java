package com.microservices;

import com.microservices.config.TwitterToKafkaServiceConfigData;
import com.microservices.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;


@SpringBootApplication
public class TwitterToKafkaServiceApplication implements CommandLineRunner {
    private final TwitterToKafkaServiceConfigData configData;
    private final StreamRunner streamRunner;

    private final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);

    public TwitterToKafkaServiceApplication(TwitterToKafkaServiceConfigData configData, StreamRunner streamRunner) {
        this.configData = configData;
        this.streamRunner = streamRunner;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class,args);
    }



    @Override
    public void run(String... args) throws Exception {
        LOG.info("App working fine so far");
        LOG.info(Arrays.toString(configData.getTwitterKeywords().toArray(new String[] {})));
        streamRunner.start();
    }
}