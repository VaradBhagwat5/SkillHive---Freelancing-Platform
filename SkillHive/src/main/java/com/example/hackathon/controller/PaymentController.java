package com.example.hackathon.controller;

import java.security.Principal;

import com.example.hackathon.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hackathon.model.Payment;
import com.example.hackathon.model.Transaction;
import com.example.hackathon.model.User;
import com.example.hackathon.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody Payment payment, Principal principal) {
        User payer = ((UserDetailsImpl) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getUser();
        payment.setPayer(payer);
        return ResponseEntity.ok(paymentService.createPayment(payment));
    }

    @PostMapping("/process")
    public ResponseEntity<?> processTransaction(@RequestBody Transaction transaction) {
        Payment payment = paymentService.createPayment(transaction.getPayment());
        return ResponseEntity.ok(paymentService.processTransaction(transaction.getTransactionId(), payment, transaction.getStatus()));
    }

    @GetMapping
    public ResponseEntity<?> getUserPayments(Principal principal) {
        User user = ((UserDetailsImpl) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getUser();
        return ResponseEntity.ok(paymentService.getUserPayments(user));
    }

    @GetMapping("/{paymentId}/transactions")
    public ResponseEntity<?> getPaymentTransactions(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentTransactions(paymentId));
    }
}

