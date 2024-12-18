package org.example.microservicio8;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VotacionService {
    private static final Logger logger = LoggerFactory.getLogger(VotacionService.class);
    private final VotanteRepository votanteRepository;
    private final PropuestasRepository propuestasRepository;
    private final List<Propuestas> propuestasAceptadas = new ArrayList<>();
    private AtomicLong tickCounter = new AtomicLong(0);
    private AtomicLong acceptedTickCounter = new AtomicLong(0);
    private Sinks.Many<Propuestas> propuestasSink = Sinks.many().multicast().onBackpressureBuffer();
    private List<String> nombresPropuestasAceptadas = new ArrayList<>();

    @Autowired
    public VotacionService(VotanteRepository votanteRepository, PropuestasRepository propuestasRepository, MeterRegistry meterRegistry) {
        this.votanteRepository = votanteRepository;
        this.propuestasRepository = propuestasRepository;
        inicializarVotantes();

        Gauge.builder("accepted_proposals_count", propuestasAceptadas, List::size)
                .description("Number of accepted proposals")
                .register(meterRegistry);
    }

    private void inicializarVotantes() {
        for (long i = 1; i <= 30; i++) {
            Votante votante = new Votante(i);
            votanteRepository.save(votante);
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
                    int presupuesto = ThreadLocalRandom.current().nextInt(100000, 300001);
                    Propuestas propuesta = new Propuestas(null, "Propuesta " + currentTick, presupuesto);
                    propuestasRepository.save(propuesta);

                    logger.info("--------tic {}-----", currentTick);

                    List<Votante> votantes = votanteRepository.findAll();
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
                                    propuesta.setAceptada(true);
                                    logger.info("Propuesta {} ({}) aceptada", propuesta.getId(), propuesta.getNombre());
                                    nombresPropuestasAceptadas.add(propuesta.toString());
                                    propuestasAceptadas.add(propuesta); // Añadir a la lista de propuestas aceptadas
                                    propuestasSink.tryEmitNext(propuesta);
                                } else {
                                    propuesta.setAceptada(false);
                                    logger.info("Propuesta {} ({}) rechazada", propuesta.getId(), propuesta.getNombre());
                                }
                                propuestasRepository.save(propuesta);
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
                    logger.info("Ultima propuesta aceptada: {}", propuestas);
                    logger.info("Nombres de propuestas aceptadas: {}", nombresPropuestasAceptadas);
                });
    }
}