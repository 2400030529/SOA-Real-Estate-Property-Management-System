package com.klu.springmvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.klu.springmvc.entity.Inquiry;

public interface InquiryRepository
        extends JpaRepository<Inquiry, Long> {
}
