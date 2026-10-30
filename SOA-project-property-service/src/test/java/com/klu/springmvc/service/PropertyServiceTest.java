
package com.klu.springmvc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.klu.springmvc.entity.Property;
import com.klu.springmvc.repository.PropertyRepository;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
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
    void testCreateProperty() {
        when(propertyRepository.save(property)).thenReturn(property);

        Property result = propertyService.createProperty(property);

        assertNotNull(result);
        assertEquals("Luxury Apartment", result.getTitle());
        verify(propertyRepository, times(1)).save(property);
    }

    @Test
    void testGetAllProperties() {
        when(propertyRepository.findAll()).thenReturn(Arrays.asList(property));

        List<Property> result = propertyService.getAllProperties();

        assertEquals(1, result.size());
        verify(propertyRepository, times(1)).findAll();
    }

    @Test
    void testGetPropertyById() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        Optional<Property> result = propertyService.getPropertyById(1L);

        assertTrue(result.isPresent());
        assertEquals("Luxury Apartment", result.get().getTitle());
    }

    @Test
    void testGetPropertyByIdNotFound() {
        when(propertyRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Property> result = propertyService.getPropertyById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateProperty() {
        Property updatedDetails = new Property();
        updatedDetails.setTitle("Updated Apartment");
        updatedDetails.setDescription("Updated description");
        updatedDetails.setLocation("Hyderabad");
        updatedDetails.setPrice(6000000.0);
        updatedDetails.setPropertyType("Villa");
        updatedDetails.setStatus("SOLD");

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(propertyRepository.save(property)).thenReturn(property);

        Property result = propertyService.updateProperty(1L, updatedDetails);

        assertEquals("Updated Apartment", result.getTitle());
        assertEquals("Hyderabad", result.getLocation());
        assertEquals(6000000.0, result.getPrice());
    }

    @Test
    void testUpdatePropertyNotFound() {
        when(propertyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> propertyService.updateProperty(99L, property));
    }

    @Test
    void testDeleteProperty() {
        when(propertyRepository.existsById(1L)).thenReturn(true);

        propertyService.deleteProperty(1L);

        verify(propertyRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePropertyNotFound() {
        when(propertyRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> propertyService.deleteProperty(99L));
    }
}
