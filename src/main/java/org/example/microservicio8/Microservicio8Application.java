package org.example.microservicio8;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@EnableDiscoveryClient
public class Microservicio8Application {

    @Autowired
    private VotacionService votacionService;

    public static void main(String[] args) {
        SpringApplication.run(Microservicio8Application.class, args);
    }

    @PostConstruct
    public void init() {
        votacionService.iniciarFlujo();
    }
}