package com.saucelabs.magnificent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MagnificientHealthService {

    static final String MAGNIFICIENT_SERVER_URL = "http://localhost:12345";

    @Autowired
    private RestTemplate restTemplate;

    public boolean isServerAlive() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(MAGNIFICIENT_SERVER_URL, String.class);

        return response.getStatusCode().is2xxSuccessful();
    }
}
