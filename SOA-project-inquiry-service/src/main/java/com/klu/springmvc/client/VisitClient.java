package com.klu.springmvc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.klu.springmvc.dto.VisitRequest;

@FeignClient(name = "SOA-PROJECT-VISIT-SERVICE")
public interface VisitClient {

    @PostMapping("/visits")
    Object createVisit(@RequestBody VisitRequest visitRequest);
}