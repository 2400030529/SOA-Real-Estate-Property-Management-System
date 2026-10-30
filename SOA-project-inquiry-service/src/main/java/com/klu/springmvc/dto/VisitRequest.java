package com.klu.springmvc.dto;

import java.time.LocalDateTime;

public class VisitRequest {

    private Long propertyId;
    private Long userId;
    private LocalDateTime visitDate;
    private String status;

    public VisitRequest() {
    }

    public VisitRequest(Long propertyId, Long userId,
                        LocalDateTime visitDate, String status) {
        this.propertyId = propertyId;
        this.userId = userId;
        this.visitDate = visitDate;
        this.status = status;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDateTime visitDate) {
        this.visitDate = visitDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}