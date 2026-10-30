package com.klu.springmvc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SOA-PROJECT-PROPERTY-SERVICE")
public interface PropertyClient {

    @GetMapping("/properties/{id}")
    Object getPropertyById(@PathVariable("id") Long id);
}
