package com.klu.springmvc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.klu.springmvc.client.PropertyClient;
import com.klu.springmvc.client.VisitClient;
import com.klu.springmvc.dto.VisitRequest;
import com.klu.springmvc.entity.Inquiry;
import com.klu.springmvc.repository.InquiryRepository;

@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final PropertyClient propertyClient;
    private final VisitClient visitClient;

    public InquiryService(
            InquiryRepository inquiryRepository,
            PropertyClient propertyClient,
            VisitClient visitClient) {
        this.inquiryRepository = inquiryRepository;
        this.propertyClient = propertyClient;
        this.visitClient = visitClient;
    }

    public Inquiry createInquiry(Inquiry inquiry) {

        propertyClient.getPropertyById(inquiry.getPropertyId());

        if (inquiry.getStatus() == null ||
                inquiry.getStatus().isBlank()) {
            inquiry.setStatus("PENDING");
        }

        inquiry.setId(null);
        return inquiryRepository.save(inquiry);
    }

    public Object scheduleVisit(Long inquiryId,
                                LocalDateTime visitDate) {

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() ->
                        new RuntimeException("Inquiry not found"));

        VisitRequest request = new VisitRequest(
                inquiry.getPropertyId(),
                inquiry.getUserId(),
                visitDate,
                "SCHEDULED"
        );

        return visitClient.createVisit(request);
    }

    public List<Inquiry> getAllInquiries() {
        return inquiryRepository.findAll();
    }

    public Optional<Inquiry> getInquiryById(Long id) {
        return inquiryRepository.findById(id);
    }

    public Inquiry updateInquiry(Long id, Inquiry details) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Inquiry not found"));

        inquiry.setMessage(details.getMessage());
        inquiry.setStatus(details.getStatus());

        return inquiryRepository.save(inquiry);
    }

    public void deleteInquiry(Long id) {
        if (!inquiryRepository.existsById(id)) {
            throw new RuntimeException("Inquiry not found");
        }

        inquiryRepository.deleteById(id);
    }
}