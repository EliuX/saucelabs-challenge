package com.saucelabs.magnificent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

@Component
public class MagnificientHealthService {

    static final String MAGNIFICIENT_SERVER_URL = "http://localhost:12345";

    private static final Logger LOG =
            Logger.getLogger(MagnificientHealthService.class.getName());

    private Queue<Boolean> healthQueue = new LinkedList<>();

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(fixedDelay = 10000)
    public void logHealth() {
        LOG.warning(
                String.format("%s: Checked at %s", isServerAlive() ? "Alive" : "Dead | %s",
                        LocalTime.now(), healthStatsDescription())
        );
    }

    private String healthStatsDescription() {
        return String.format("%s of the times has been alive", healthStats());
    }

    public boolean isServerAlive() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(MAGNIFICIENT_SERVER_URL, String.class);

        return response.getStatusCode().is2xxSuccessful();
    }

    void queueHealthResult(boolean isAlive) {
        if (healthQueue.size() >= 10) {
            healthQueue.remove();
        }

        healthQueue.add(isAlive);
    }

    Long healthStats() {
        Integer sum = healthQueue.stream().mapToInt(i -> i ? 1 : 0).sum();
        return Math.round(Double.valueOf(sum) * 100 / Double.valueOf(healthQueue.size()));
    }
}
