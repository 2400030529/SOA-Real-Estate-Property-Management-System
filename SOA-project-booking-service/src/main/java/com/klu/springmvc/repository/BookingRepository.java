package com.klu.springmvc.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import com.klu.springmvc.entity.Booking;

public interface BookingRepository
        extends JpaRepository<Booking, Long> {
}
