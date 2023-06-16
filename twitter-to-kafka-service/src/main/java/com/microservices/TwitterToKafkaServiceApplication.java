package com.microservices;


import com.microservices.init.StreamInitializer;
import com.microservices.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
public class TwitterToKafkaServiceApplication implements CommandLineRunner {
    private final StreamRunner streamRunner;

    private final StreamInitializer streamInitializer;
    private final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);

    public TwitterToKafkaServiceApplication(StreamRunner streamRunner, StreamInitializer streamInitializer) {
        this.streamRunner = streamRunner;
        this.streamInitializer = streamInitializer;
    }


    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class,args);
    }



    @Override
    public void run(String... args) throws Exception {
        LOG.info("App working fine so far");
        streamInitializer.init();
        streamRunner.start();
    }
}