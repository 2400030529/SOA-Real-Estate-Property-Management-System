package com.klu.springmvc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.klu.springmvc.client.PropertyClient;
import com.klu.springmvc.entity.Booking;
import com.klu.springmvc.repository.BookingRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyClient propertyClient;

    public BookingService(
            BookingRepository bookingRepository,
            PropertyClient propertyClient) {
        this.bookingRepository = bookingRepository;
        this.propertyClient = propertyClient;
    }

    public Booking createBooking(Booking booking) {

        // Verify property through Property Service
        propertyClient.getPropertyById(booking.getPropertyId());

        booking.setId(null);

        if (booking.getStatus() == null
                || booking.getStatus().isBlank()) {
            booking.setStatus("PENDING");
        }

        if (booking.getBookingDate() == null) {
            booking.setBookingDate(java.time.LocalDateTime.now());
        }

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking updateBooking(Long id, Booking details) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Booking not found"));

        booking.setPropertyId(details.getPropertyId());
        booking.setUserId(details.getUserId());
        booking.setBookingAmount(details.getBookingAmount());
        booking.setBookingDate(details.getBookingDate());
        booking.setStatus(details.getStatus());

        return bookingRepository.save(booking);
    }

    public boolean deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            return false;
        }

        bookingRepository.deleteById(id);
        return true;
    }
}