package org.example.microservicio8;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    private final VotacionService votacionService;

    public RabbitMQListener(VotacionService votacionService) {
        this.votacionService = votacionService;
    }

    @RabbitListener(queues = "queueName")
    public void listen(String message) {
        votacionService.procesarPropuesta();
    }
}