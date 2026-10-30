package com.klu.springmvc.service;

import com.klu.springmvc.entity.Property;
import com.klu.springmvc.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    // Create a property
    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    // Get all properties
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    // Get property by ID
    public Optional<Property> getPropertyById(Long id) {
        return propertyRepository.findById(id);
    }

    // Update property
    public Property updateProperty(Long id, Property propertyDetails) {

        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        property.setTitle(propertyDetails.getTitle());
        property.setDescription(propertyDetails.getDescription());
        property.setLocation(propertyDetails.getLocation());
        property.setPrice(propertyDetails.getPrice());
        property.setPropertyType(propertyDetails.getPropertyType());
        property.setStatus(propertyDetails.getStatus());

        return propertyRepository.save(property);
    }

    // Delete property
    public void deleteProperty(Long id) {

        if (!propertyRepository.existsById(id)) {
            throw new RuntimeException("Property not found");
        }

        propertyRepository.deleteById(id);
    }
}