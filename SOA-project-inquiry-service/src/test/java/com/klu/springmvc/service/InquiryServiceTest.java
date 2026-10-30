
package com.klu.springmvc.service;

import static org.junit.jupiter.api.Assertions.*;
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
import com.klu.springmvc.client.VisitClient;
import com.klu.springmvc.dto.VisitRequest;
import com.klu.springmvc.entity.Inquiry;
import com.klu.springmvc.repository.InquiryRepository;

@ExtendWith(MockitoExtension.class)
class InquiryServiceTest {

    @Mock
    private InquiryRepository inquiryRepository;

    @Mock
    private PropertyClient propertyClient;

    @Mock
    private VisitClient visitClient;

    @InjectMocks
    private InquiryService inquiryService;

    private Inquiry inquiry;

    @BeforeEach
    void setUp() {
        inquiry = new Inquiry();
        inquiry.setId(1L);
        inquiry.setPropertyId(1L);
        inquiry.setUserId(1L);
        inquiry.setMessage("I am interested in this property");
        inquiry.setStatus("PENDING");
    }

    @Test
    void testCreateInquiry() {
        when(inquiryRepository.save(inquiry)).thenReturn(inquiry);

        Inquiry result = inquiryService.createInquiry(inquiry);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertNull(inquiry.getId());
        verify(propertyClient, times(1)).getPropertyById(1L);
        verify(inquiryRepository, times(1)).save(inquiry);
    }

    @Test
    void testCreateInquirySetsDefaultStatus() {
        inquiry.setStatus(null);

        when(inquiryRepository.save(inquiry)).thenReturn(inquiry);

        Inquiry result = inquiryService.createInquiry(inquiry);

        assertEquals("PENDING", result.getStatus());
        verify(propertyClient).getPropertyById(1L);
    }

    @Test
    void testCreateInquiryWithBlankStatus() {
        inquiry.setStatus("   ");

        when(inquiryRepository.save(inquiry)).thenReturn(inquiry);

        Inquiry result = inquiryService.createInquiry(inquiry);

        assertEquals("PENDING", result.getStatus());
    }

    @Test
    void testScheduleVisit() {
        LocalDateTime visitDate = LocalDateTime.of(2026, 11, 5, 10, 0);

        when(inquiryRepository.findById(1L))
                .thenReturn(Optional.of(inquiry));
        when(visitClient.createVisit(any(VisitRequest.class)))
                .thenReturn("Visit scheduled");

        Object result = inquiryService.scheduleVisit(1L, visitDate);

        assertEquals("Visit scheduled", result);

        verify(visitClient).createVisit(argThat(request ->
                request.getPropertyId().equals(1L)
                && request.getUserId().equals(1L)
                && request.getVisitDate().equals(visitDate)
                && request.getStatus().equals("SCHEDULED")
        ));
    }

    @Test
    void testScheduleVisitInquiryNotFound() {
        when(inquiryRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> inquiryService.scheduleVisit(
                        99L, LocalDateTime.now()));

        verify(visitClient, never()).createVisit(any());
    }

    @Test
    void testGetAllInquiries() {
        when(inquiryRepository.findAll())
                .thenReturn(Arrays.asList(inquiry));

        List<Inquiry> result = inquiryService.getAllInquiries();

        assertEquals(1, result.size());
        verify(inquiryRepository).findAll();
    }

    @Test
    void testGetInquiryById() {
        when(inquiryRepository.findById(1L))
                .thenReturn(Optional.of(inquiry));

        Optional<Inquiry> result = inquiryService.getInquiryById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetInquiryByIdNotFound() {
        when(inquiryRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Inquiry> result = inquiryService.getInquiryById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateInquiry() {
        Inquiry details = new Inquiry();
        details.setMessage("Please provide more details");
        details.setStatus("IN_PROGRESS");

        when(inquiryRepository.findById(1L))
                .thenReturn(Optional.of(inquiry));
        when(inquiryRepository.save(inquiry))
                .thenReturn(inquiry);

        Inquiry result = inquiryService.updateInquiry(1L, details);

        assertEquals("Please provide more details", result.getMessage());
        assertEquals("IN_PROGRESS", result.getStatus());
        verify(inquiryRepository).save(inquiry);
    }

    @Test
    void testUpdateInquiryNotFound() {
        when(inquiryRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> inquiryService.updateInquiry(99L, inquiry));
    }

    @Test
    void testDeleteInquiry() {
        when(inquiryRepository.existsById(1L)).thenReturn(true);

        inquiryService.deleteInquiry(1L);

        verify(inquiryRepository).deleteById(1L);
    }

    @Test
    void testDeleteInquiryNotFound() {
        when(inquiryRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> inquiryService.deleteInquiry(99L));

        verify(inquiryRepository, never()).deleteById(99L);
    }
}
