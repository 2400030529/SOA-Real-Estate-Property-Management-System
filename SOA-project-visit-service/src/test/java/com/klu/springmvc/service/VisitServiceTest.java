
package com.klu.springmvc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.klu.springmvc.client.PropertyClient;
import com.klu.springmvc.entity.Visit;
import com.klu.springmvc.repository.VisitRepository;

@ExtendWith(MockitoExtension.class)
class VisitServiceTest {

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private PropertyClient propertyClient;

    @InjectMocks
    private VisitService visitService;

    private Visit visit;

    @BeforeEach
    void setUp() {
        visit = new Visit();
        visit.setId(1L);
        visit.setPropertyId(1L);
        visit.setUserId(1L);
        visit.setVisitDate(LocalDateTime.of(2026, 11, 5, 10, 0));
        visit.setStatus("SCHEDULED");
    }

    @Test
    void testCreateVisit() {
        when(visitRepository.save(visit)).thenReturn(visit);

        Visit result = visitService.createVisit(visit);

        assertNotNull(result);
        assertEquals("SCHEDULED", result.getStatus());
        assertNull(visit.getId());
        verify(propertyClient).getPropertyById(1L);
        verify(visitRepository).save(visit);
    }

    @Test
    void testCreateVisitSetsDefaultStatus() {
        visit.setStatus(null);
        when(visitRepository.save(visit)).thenReturn(visit);

        Visit result = visitService.createVisit(visit);

        assertEquals("SCHEDULED", result.getStatus());
    }

    @Test
    void testCreateVisitWithBlankStatus() {
        visit.setStatus("   ");
        when(visitRepository.save(visit)).thenReturn(visit);

        Visit result = visitService.createVisit(visit);

        assertEquals("SCHEDULED", result.getStatus());
    }

    @Test
    void testCreateVisitCallsPropertyService() {
        when(visitRepository.save(visit)).thenReturn(visit);

        visitService.createVisit(visit);

        verify(propertyClient, times(1)).getPropertyById(1L);
    }

    @Test
    void testGetAllVisits() {
        when(visitRepository.findAll()).thenReturn(Arrays.asList(visit));

        List<Visit> result = visitService.getAllVisits();

        assertEquals(1, result.size());
        verify(visitRepository).findAll();
    }

    @Test
    void testGetVisitById() {
        when(visitRepository.findById(1L))
                .thenReturn(Optional.of(visit));

        Optional<Visit> result = visitService.getVisitById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetVisitByIdNotFound() {
        when(visitRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Visit> result = visitService.getVisitById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateVisit() {
        Visit details = new Visit();
        details.setPropertyId(2L);
        details.setUserId(2L);
        details.setVisitDate(LocalDateTime.of(2026, 11, 6, 11, 0));
        details.setStatus("COMPLETED");

        when(visitRepository.findById(1L))
                .thenReturn(Optional.of(visit));
        when(visitRepository.save(visit)).thenReturn(visit);

        Visit result = visitService.updateVisit(1L, details);

        assertEquals(2L, result.getPropertyId());
        assertEquals(2L, result.getUserId());
        assertEquals("COMPLETED", result.getStatus());
        assertEquals(details.getVisitDate(), result.getVisitDate());
    }

    @Test
    void testUpdateVisitNotFound() {
        when(visitRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> visitService.updateVisit(99L, visit));
    }

    @Test
    void testDeleteVisit() {
        when(visitRepository.existsById(1L)).thenReturn(true);

        boolean result = visitService.deleteVisit(1L);

        assertTrue(result);
        verify(visitRepository).deleteById(1L);
    }

    @Test
    void testDeleteVisitNotFound() {
        when(visitRepository.existsById(99L)).thenReturn(false);

        boolean result = visitService.deleteVisit(99L);

        assertFalse(result);
        verify(visitRepository, never()).deleteById(99L);
    }
}
