package com.saucelabs.magnificent.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static com.saucelabs.magnificent.services.MagnificientHealthService.MAGNIFICIENT_SERVER_URL;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MagnificientHealthServiceTest {

    @LocalServerPort
    private Integer port;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private MagnificientHealthService healthService;

    @Test
    public void whenServerIsDown_whenCallServerIsAlive_itShouldReturnFalse() {
        Mockito.when(restTemplate.getForEntity(MAGNIFICIENT_SERVER_URL, String.class))
                .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Bad content"));

        Assert.assertFalse("Server should be notified as dead", healthService.isServerAlive());
    }

    @Test
    public void whenServerIsUp_whenCallServerIsAlive_itShouldReturnTrue() {
        Mockito.when(restTemplate.getForEntity(MAGNIFICIENT_SERVER_URL, String.class))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body("Magnificent!"));

        Assert.assertTrue("Server should be notified as alive", healthService.isServerAlive());
    }
}