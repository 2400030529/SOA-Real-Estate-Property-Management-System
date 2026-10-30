package com.klu.springmvc.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klu.springmvc.entity.Inquiry;
import com.klu.springmvc.service.InquiryService;

@RestController
@RequestMapping("/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping
    public ResponseEntity<Inquiry> createInquiry(
            @RequestBody Inquiry inquiry) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inquiryService.createInquiry(inquiry));
    }

    @PostMapping("/{id}/schedule-visit")
    public ResponseEntity<Object> scheduleVisit(
            @PathVariable Long id,
            @RequestBody VisitScheduleRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inquiryService.scheduleVisit(
                        id, request.getVisitDate()));
    }

    @GetMapping
    public ResponseEntity<List<Inquiry>> getAllInquiries() {
        return ResponseEntity.ok(
                inquiryService.getAllInquiries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inquiry> getInquiryById(
            @PathVariable Long id) {
        return inquiryService.getInquiryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inquiry> updateInquiry(
            @PathVariable Long id,
            @RequestBody Inquiry details) {
        return ResponseEntity.ok(
                inquiryService.updateInquiry(id, details));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInquiry(
            @PathVariable Long id) {
        inquiryService.deleteInquiry(id);
        return ResponseEntity.noContent().build();
    }

    public static class VisitScheduleRequest {
        private LocalDateTime visitDate;

        public LocalDateTime getVisitDate() {
            return visitDate;
        }

        public void setVisitDate(LocalDateTime visitDate) {
            this.visitDate = visitDate;
        }
    }
}