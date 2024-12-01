package org.example.microservicio8;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VotacionService {
    private static final Logger logger = LoggerFactory.getLogger(VotacionService.class);
    private List<Votante> votantes = new ArrayList<>();
    private AtomicLong tickCounter = new AtomicLong(0);
    private AtomicLong acceptedTickCounter = new AtomicLong(0);
    private List<Propuestas> propuestasAceptadas = new ArrayList<>();
    private Sinks.Many<Propuestas> propuestasSink = Sinks.many().multicast().onBackpressureBuffer();

    public VotacionService() {
        for (long i = 1; i <= 30; i++) {
            votantes.add(new Votante(i));
        }
    }

    public void iniciarFlujo() {
        procesarPropuesta();
        procesarPropuestasAceptadas();
    }

    public void procesarPropuesta() {
        Flux.interval(Duration.ofSeconds(4))
            .flatMap(tick -> {
                long currentTick = tickCounter.incrementAndGet();
                Propuestas propuesta = new Propuestas(System.currentTimeMillis(), "Propuesta " + currentTick);
                logger.info("--------tic {}-----", currentTick);
                return Flux.fromIterable(votantes)
                    .map(votante -> {
                        boolean votoAFavor = votante.votar();
                        logger.info("Votante {}: {}", votante.getId(), votoAFavor ? "a favor" : "en contra");
                        return votoAFavor;
                    })
                    .collectList()
                    .map(votos -> {
                        long votosAFavor = votos.stream().filter(voto -> voto).count();
                        if (votosAFavor >= 15) {
                            logger.info("Propuesta {} ({}) aceptada", propuesta.getId(), propuesta.getNombre());
                            propuestasAceptadas.add(propuesta);
                            propuestasSink.tryEmitNext(propuesta);
                        } else {
                            logger.info("Propuesta {} ({}) rechazada", propuesta.getId(), propuesta.getNombre());
                        }
                        return votosAFavor;
                    });
            })
            .subscribe();
    }

    public void procesarPropuestasAceptadas() {
        propuestasSink.asFlux()
            .buffer(Duration.ofSeconds(4))
            .subscribe(propuestas -> {
                long acceptedTick = acceptedTickCounter.incrementAndGet();
                logger.info("--------tic {}-----", acceptedTick);
                logger.info("Propuestas aceptadas: {}", propuestasAceptadas);
            });
    }
}