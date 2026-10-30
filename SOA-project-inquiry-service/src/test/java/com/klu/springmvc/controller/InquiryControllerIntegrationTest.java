
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

import com.klu.springmvc.entity.Inquiry;
import com.klu.springmvc.service.InquiryService;

@WebMvcTest(InquiryController.class)
class InquiryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InquiryService inquiryService;

    private Inquiry inquiry;

    @BeforeEach
    void setUp() {
        inquiry = new Inquiry();
        inquiry.setId(1L);
        inquiry.setPropertyId(1L);
        inquiry.setUserId(1L);
        inquiry.setMessage("Interested in this property");
        inquiry.setStatus("PENDING");
    }

    @Test
    void testCreateInquiry() throws Exception {
        when(inquiryService.createInquiry(any(Inquiry.class)))
                .thenReturn(inquiry);

        mockMvc.perform(post("/inquiries")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"propertyId\":1,"
                        + "\"userId\":1,"
                        + "\"message\":\"Interested in this property\","
                        + "\"status\":\"PENDING\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.message")
                        .value("Interested in this property"));
    }

    @Test
    void testScheduleVisit() throws Exception {
        when(inquiryService.scheduleVisit(
                eq(1L), any(LocalDateTime.class)))
                .thenReturn("Visit scheduled successfully");

        mockMvc.perform(post("/inquiries/1/schedule-visit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"visitDate\":\"2026-11-05T10:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(
                        "Visit scheduled successfully"));
    }

    @Test
    void testGetAllInquiries() throws Exception {
        when(inquiryService.getAllInquiries())
                .thenReturn(Arrays.asList(inquiry));

        mockMvc.perform(get("/inquiries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].message")
                        .value("Interested in this property"));
    }

    @Test
    void testGetInquiryById() throws Exception {
        when(inquiryService.getInquiryById(1L))
                .thenReturn(Optional.of(inquiry));

        mockMvc.perform(get("/inquiries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetInquiryByIdNotFound() throws Exception {
        when(inquiryService.getInquiryById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/inquiries/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateInquiry() throws Exception {
        when(inquiryService.updateInquiry(
                eq(1L), any(Inquiry.class)))
                .thenReturn(inquiry);

        mockMvc.perform(put("/inquiries/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"message\":\"Interested in this property\","
                        + "\"status\":\"PENDING\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testDeleteInquiry() throws Exception {
        doNothing().when(inquiryService).deleteInquiry(1L);

        mockMvc.perform(delete("/inquiries/1"))
                .andExpect(status().isNoContent());

        verify(inquiryService).deleteInquiry(1L);
    }
}
