
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
import com.klu.springmvc.entity.Booking;
import com.klu.springmvc.repository.BookingRepository;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PropertyClient propertyClient;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setId(1L);
        booking.setPropertyId(1L);
        booking.setUserId(1L);
        booking.setBookingAmount(5000000.0);
        booking.setBookingDate(LocalDateTime.of(2026, 11, 5, 10, 0));
        booking.setStatus("PENDING");
    }

    @Test
    void testCreateBooking() {
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking result = bookingService.createBooking(booking);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertNull(booking.getId());
        verify(propertyClient).getPropertyById(1L);
        verify(bookingRepository).save(booking);
    }

    @Test
    void testCreateBookingSetsDefaultStatus() {
        booking.setStatus(null);
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking result = bookingService.createBooking(booking);

        assertEquals("PENDING", result.getStatus());
    }

    @Test
    void testCreateBookingWithBlankStatus() {
        booking.setStatus("   ");
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking result = bookingService.createBooking(booking);

        assertEquals("PENDING", result.getStatus());
    }

    @Test
    void testCreateBookingSetsDefaultBookingDate() {
        booking.setBookingDate(null);
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking result = bookingService.createBooking(booking);

        assertNotNull(result.getBookingDate());
    }

    @Test
    void testCreateBookingCallsPropertyService() {
        when(bookingRepository.save(booking)).thenReturn(booking);

        bookingService.createBooking(booking);

        verify(propertyClient, times(1)).getPropertyById(1L);
    }

    @Test
    void testGetAllBookings() {
        when(bookingRepository.findAll())
                .thenReturn(Arrays.asList(booking));

        List<Booking> result = bookingService.getAllBookings();

        assertEquals(1, result.size());
        verify(bookingRepository).findAll();
    }

    @Test
    void testGetBookingById() {
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        Optional<Booking> result = bookingService.getBookingById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetBookingByIdNotFound() {
        when(bookingRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Booking> result = bookingService.getBookingById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateBooking() {
        Booking details = new Booking();
        details.setPropertyId(2L);
        details.setUserId(2L);
        details.setBookingAmount(6000000.0);
        details.setBookingDate(LocalDateTime.of(2026, 11, 6, 11, 0));
        details.setStatus("CONFIRMED");

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking result = bookingService.updateBooking(1L, details);

        assertEquals(2L, result.getPropertyId());
        assertEquals(2L, result.getUserId());
        assertEquals(6000000.0, result.getBookingAmount());
        assertEquals("CONFIRMED", result.getStatus());
        assertEquals(details.getBookingDate(), result.getBookingDate());
    }

    @Test
    void testUpdateBookingNotFound() {
        when(bookingRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> bookingService.updateBooking(99L, booking));
    }

    @Test
    void testDeleteBooking() {
        when(bookingRepository.existsById(1L)).thenReturn(true);

        boolean result = bookingService.deleteBooking(1L);

        assertTrue(result);
        verify(bookingRepository).deleteById(1L);
    }

    @Test
    void testDeleteBookingNotFound() {
        when(bookingRepository.existsById(99L)).thenReturn(false);

        boolean result = bookingService.deleteBooking(99L);

        assertFalse(result);
        verify(bookingRepository, never()).deleteById(99L);
    }
}
