package com.klu.springmvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // Test Property Service routing
    @Test
    void testPropertyServiceRouting() {

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/properties",
                String.class
        );
        
        System.out.println("Property Gateway Status: " + response.getStatusCode());
        System.out.println("Property Gateway Body: " + response.getBody());

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
    }

    // Test Inquiry Service routing
    @Test
    void testInquiryServiceRouting() {

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/inquiries",
                String.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
    }

    // Test Visit Service routing
    @Test
    void testVisitServiceRouting() {

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/visits",
                String.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
    }

    // Test Booking Service routing
    @Test
    void testBookingServiceRouting() {

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/bookings",
                String.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
    }
}