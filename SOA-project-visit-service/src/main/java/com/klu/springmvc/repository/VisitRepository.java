package com.klu.springmvc.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.klu.springmvc.entity.Visit;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}
