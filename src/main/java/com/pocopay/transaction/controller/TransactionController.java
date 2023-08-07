package com.pocopay.transaction.controller;

import com.pocopay.transaction.model.CreateTransactionRequestDto;
import com.pocopay.transaction.model.CreateTransactionResponseDto;
import com.pocopay.transaction.model.TransactionDto;
import com.pocopay.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping
    public ResponseEntity<CreateTransactionResponseDto> initiateTransaction(@RequestBody CreateTransactionRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.initTransaction(requestDto));
    }

    @PatchMapping
    public ResponseEntity<Void> executeTransaction(@RequestHeader("cif") String cif,
                                                   @RequestBody String transactionId) {
        service.executeTransaction(cif, transactionId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDto> getTransaction(@RequestHeader("cif") String cif,
                                                         @PathVariable("transactionId") String transactionId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getTransaction(cif, transactionId));
    }
}
