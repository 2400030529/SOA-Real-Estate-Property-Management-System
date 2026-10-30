
package com.klu.springmvc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.klu.springmvc.entity.Visit;
import com.klu.springmvc.service.VisitService;

@WebMvcTest(VisitController.class)
class VisitControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
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
    void testCreateVisit() throws Exception {
        when(visitService.createVisit(any(Visit.class)))
                .thenReturn(visit);

        mockMvc.perform(post("/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"propertyId\":1,"
                        + "\"userId\":1,"
                        + "\"visitDate\":\"2026-11-05T10:00:00\","
                        + "\"status\":\"SCHEDULED\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void testGetAllVisits() throws Exception {
        when(visitService.getAllVisits())
                .thenReturn(Arrays.asList(visit));

        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("SCHEDULED"));
    }

    @Test
    void testGetVisitById() throws Exception {
        when(visitService.getVisitById(1L))
                .thenReturn(Optional.of(visit));

        mockMvc.perform(get("/visits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetVisitByIdNotFound() throws Exception {
        when(visitService.getVisitById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/visits/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateVisit() throws Exception {
        when(visitService.getVisitById(1L))
                .thenReturn(Optional.of(visit));
        when(visitService.updateVisit(eq(1L), any(Visit.class)))
                .thenReturn(visit);

        mockMvc.perform(put("/visits/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"propertyId\":1,"
                        + "\"userId\":1,"
                        + "\"visitDate\":\"2026-11-05T10:00:00\","
                        + "\"status\":\"SCHEDULED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateVisitNotFound() throws Exception {
        when(visitService.getVisitById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/visits/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"propertyId\":1,"
                        + "\"userId\":1,"
                        + "\"visitDate\":\"2026-11-05T10:00:00\","
                        + "\"status\":\"SCHEDULED\"}"))
                .andExpect(status().isNotFound());

        verify(visitService, never()).updateVisit(
                eq(99L), any(Visit.class));
    }

    @Test
    void testDeleteVisit() throws Exception {
        when(visitService.deleteVisit(1L)).thenReturn(true);

        mockMvc.perform(delete("/visits/1"))
                .andExpect(status().isNoContent());

        verify(visitService).deleteVisit(1L);
    }

    @Test
    void testDeleteVisitNotFound() throws Exception {
        when(visitService.deleteVisit(99L)).thenReturn(false);

        mockMvc.perform(delete("/visits/99"))
                .andExpect(status().isNotFound());
    }
}
