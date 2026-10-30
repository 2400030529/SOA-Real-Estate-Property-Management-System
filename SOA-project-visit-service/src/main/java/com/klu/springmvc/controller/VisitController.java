package com.klu.springmvc.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klu.springmvc.entity.Visit;
import com.klu.springmvc.service.VisitService;

@RestController
@RequestMapping("/visits")
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping
    public ResponseEntity<Visit> createVisit(@RequestBody Visit visit) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(visitService.createVisit(visit));
    }

    @GetMapping
    public ResponseEntity<List<Visit>> getAllVisits() {
        return ResponseEntity.ok(visitService.getAllVisits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visit> getVisitById(@PathVariable Long id) {
        return visitService.getVisitById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Visit> updateVisit(
            @PathVariable Long id,
            @RequestBody Visit details) {

        return visitService.getVisitById(id)
            .map(existing -> {
                details.setId(id);
                return ResponseEntity.ok(visitService.updateVisit(id, details));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long id) {
        return visitService.deleteVisit(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}
