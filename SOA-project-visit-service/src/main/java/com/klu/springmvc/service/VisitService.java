package com.klu.springmvc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.klu.springmvc.client.PropertyClient;
import com.klu.springmvc.entity.Visit;
import com.klu.springmvc.repository.VisitRepository;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final PropertyClient propertyClient;

    public VisitService(
            VisitRepository visitRepository,
            PropertyClient propertyClient) {
        this.visitRepository = visitRepository;
        this.propertyClient = propertyClient;
    }

    public Visit createVisit(Visit visit) {

        // Verify property through Property Service
        propertyClient.getPropertyById(visit.getPropertyId());

        visit.setId(null);

        if (visit.getStatus() == null ||
                visit.getStatus().isBlank()) {
            visit.setStatus("SCHEDULED");
        }

        return visitRepository.save(visit);
    }

    public List<Visit> getAllVisits() {
        return visitRepository.findAll();
    }

    public Optional<Visit> getVisitById(Long id) {
        return visitRepository.findById(id);
    }

    public Visit updateVisit(Long id, Visit details) {
        Visit visit = visitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Visit not found"));

        visit.setPropertyId(details.getPropertyId());
        visit.setUserId(details.getUserId());
        visit.setVisitDate(details.getVisitDate());
        visit.setStatus(details.getStatus());

        return visitRepository.save(visit);
    }

    public boolean deleteVisit(Long id) {
        if (!visitRepository.existsById(id)) {
            return false;
        }

        visitRepository.deleteById(id);
        return true;
    }
}