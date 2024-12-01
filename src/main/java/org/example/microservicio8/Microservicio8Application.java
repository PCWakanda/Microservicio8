package org.example.microservicio8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Microservicio8Application {

    public static void main(String[] args) {
        SpringApplication.run(Microservicio8Application.class, args);
    }

}
