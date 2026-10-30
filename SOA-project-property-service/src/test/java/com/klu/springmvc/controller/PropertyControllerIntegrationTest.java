
package com.klu.springmvc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.klu.springmvc.entity.Property;
import com.klu.springmvc.service.PropertyService;

@WebMvcTest(PropertyController.class)
class PropertyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PropertyService propertyService;

    private Property property;

    @BeforeEach
    void setUp() {
        property = new Property();
        property.setId(1L);
        property.setTitle("Luxury Apartment");
        property.setDescription("Beautiful apartment");
        property.setLocation("Vijayawada");
        property.setPrice(5000000.0);
        property.setPropertyType("Apartment");
        property.setStatus("AVAILABLE");
    }

    @Test
    void testCreateProperty() throws Exception {
        when(propertyService.createProperty(any(Property.class)))
                .thenReturn(property);

        mockMvc.perform(post("/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Luxury Apartment\","
                        + "\"description\":\"Beautiful apartment\","
                        + "\"location\":\"Vijayawada\","
                        + "\"price\":5000000.0,"
                        + "\"propertyType\":\"Apartment\","
                        + "\"status\":\"AVAILABLE\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Luxury Apartment"));
    }

    @Test
    void testGetAllProperties() throws Exception {
        when(propertyService.getAllProperties())
                .thenReturn(Arrays.asList(property));

        mockMvc.perform(get("/properties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Luxury Apartment"));
    }

    @Test
    void testGetPropertyById() throws Exception {
        when(propertyService.getPropertyById(1L))
                .thenReturn(Optional.of(property));

        mockMvc.perform(get("/properties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.location").value("Vijayawada"));
    }

    @Test
    void testGetPropertyByIdNotFound() throws Exception {
        when(propertyService.getPropertyById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/properties/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProperty() throws Exception {
        when(propertyService.updateProperty(
                Mockito.eq(1L), any(Property.class)))
                .thenReturn(property);

        mockMvc.perform(put("/properties/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Luxury Apartment\","
                        + "\"description\":\"Beautiful apartment\","
                        + "\"location\":\"Vijayawada\","
                        + "\"price\":5000000.0,"
                        + "\"propertyType\":\"Apartment\","
                        + "\"status\":\"AVAILABLE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Luxury Apartment"));
    }

    @Test
    void testDeleteProperty() throws Exception {
        doNothing().when(propertyService).deleteProperty(1L);

        mockMvc.perform(delete("/properties/1"))
                .andExpect(status().isNoContent());

        verify(propertyService).deleteProperty(1L);
    }
}
