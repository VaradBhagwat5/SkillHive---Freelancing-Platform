package com.example.hackathon.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hackathon.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByPaymentId(Long paymentId);
}
