package com.example.hackathon.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hackathon.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPayerId(Long payerId);

}
