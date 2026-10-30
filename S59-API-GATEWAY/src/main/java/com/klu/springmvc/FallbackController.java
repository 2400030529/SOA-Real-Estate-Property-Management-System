package com.klu.springmvc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/ms1")
    public String ms1Fallback() {
        return "MS1 Service is DOWN. Please try later.";
    }

    @GetMapping("/fallback/ms2")
    public String ms2Fallback() {
        return "MS2 Service is DOWN. Please try later.";
    }
}