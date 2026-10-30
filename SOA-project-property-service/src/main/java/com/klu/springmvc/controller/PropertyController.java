package com.klu.springmvc.controller;

import com.klu.springmvc.entity.Property;
import com.klu.springmvc.service.PropertyService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;

    @Value("${server.port}")
    private String serverPort;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    // Create a property
    @PostMapping
    public ResponseEntity<Property> createProperty(
            @RequestBody Property property) {

        System.out.println("REQUEST HANDLED BY PROPERTY SERVICE PORT: " + serverPort);

        Property savedProperty = propertyService.createProperty(property);

        return new ResponseEntity<>(savedProperty, HttpStatus.CREATED);
    }

    // Get all properties
    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {

        System.out.println("REQUEST HANDLED BY PROPERTY SERVICE PORT: " + serverPort);

        List<Property> properties = propertyService.getAllProperties();

        return ResponseEntity.ok(properties);
    }

    // Get property by ID
    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(
            @PathVariable Long id) {

        System.out.println("REQUEST HANDLED BY PROPERTY SERVICE PORT: " + serverPort);

        return propertyService.getPropertyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update property
    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(
            @PathVariable Long id,
            @RequestBody Property propertyDetails) {

        System.out.println("REQUEST HANDLED BY PROPERTY SERVICE PORT: " + serverPort);

        Property updatedProperty =
                propertyService.updateProperty(id, propertyDetails);

        return ResponseEntity.ok(updatedProperty);
    }

    // Delete property
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(
            @PathVariable Long id) {

        System.out.println("REQUEST HANDLED BY PROPERTY SERVICE PORT: " + serverPort);

        propertyService.deleteProperty(id);

        return ResponseEntity.noContent().build();
    }
}