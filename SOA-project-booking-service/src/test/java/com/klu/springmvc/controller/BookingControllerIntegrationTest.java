
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

import com.klu.springmvc.entity.Booking;
import com.klu.springmvc.service.BookingService;

@WebMvcTest(BookingController.class)
class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setId(1L);
        booking.setPropertyId(1L);
        booking.setUserId(1L);
        booking.setBookingAmount(5000000.0);
        booking.setBookingDate(
                LocalDateTime.of(2026, 11, 5, 10, 0));
        booking.setStatus("PENDING");
    }

    @Test
    void testCreateBooking() throws Exception {
        when(bookingService.createBooking(any(Booking.class)))
                .thenReturn(booking);

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"propertyId\":1,"
                        + "\"userId\":1,"
                        + "\"bookingAmount\":5000000.0,"
                        + "\"bookingDate\":\"2026-11-05T10:00:00\","
                        + "\"status\":\"PENDING\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testGetAllBookings() throws Exception {
        when(bookingService.getAllBookings())
                .thenReturn(Arrays.asList(booking));

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void testGetBookingById() throws Exception {
        when(bookingService.getBookingById(1L))
                .thenReturn(Optional.of(booking));

        mockMvc.perform(get("/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetBookingByIdNotFound() throws Exception {
        when(bookingService.getBookingById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/bookings/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateBooking() throws Exception {
        when(bookingService.getBookingById(1L))
                .thenReturn(Optional.of(booking));
        when(bookingService.updateBooking(
                eq(1L), any(Booking.class)))
                .thenReturn(booking);

        mockMvc.perform(put("/bookings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"propertyId\":1,"
                        + "\"userId\":1,"
                        + "\"bookingAmount\":5000000.0,"
                        + "\"bookingDate\":\"2026-11-05T10:00:00\","
                        + "\"status\":\"PENDING\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateBookingNotFound() throws Exception {
        when(bookingService.getBookingById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/bookings/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"propertyId\":1,"
                        + "\"userId\":1,"
                        + "\"bookingAmount\":5000000.0,"
                        + "\"status\":\"PENDING\"}"))
                .andExpect(status().isNotFound());

        verify(bookingService, never()).updateBooking(
                eq(99L), any(Booking.class));
    }

    @Test
    void testDeleteBooking() throws Exception {
        when(bookingService.deleteBooking(1L))
                .thenReturn(true);

        mockMvc.perform(delete("/bookings/1"))
                .andExpect(status().isNoContent());

        verify(bookingService).deleteBooking(1L);
    }

    @Test
    void testDeleteBookingNotFound() throws Exception {
        when(bookingService.deleteBooking(99L))
                .thenReturn(false);

        mockMvc.perform(delete("/bookings/99"))
                .andExpect(status().isNotFound());
    }
}
