package com.example.hackathon.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hackathon.model.Payment;
import com.example.hackathon.model.Transaction;
import com.example.hackathon.model.User;
import com.example.hackathon.repositories.PaymentRepository;
import com.example.hackathon.repositories.TransactionRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Payment createPayment(Payment payment) {
        payment.setTimestamp(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    public Transaction processTransaction(String transactionId, Payment payment, String status) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setPayment(payment);
        transaction.setStatus(status);
        transaction.setAmount(payment.getAmount());
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    public List<Payment> getUserPayments(User user) {
        return paymentRepository.findByPayerId(user.getId());
    }

    public List<Transaction> getPaymentTransactions(Long paymentId) {
        return transactionRepository.findByPaymentId(paymentId);
    }
}

