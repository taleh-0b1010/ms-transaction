package com.pocopay.transaction.service;

import com.pocopay.transaction.dao.TransactionRepository;
import com.pocopay.transaction.dao.entity.TransactionEntity;
import com.pocopay.transaction.mapper.TransactionMapper;
import com.pocopay.transaction.model.CreateTransactionRequestDto;
import com.pocopay.transaction.model.CreateTransactionResponseDto;
import com.pocopay.transaction.queue.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final AccountService accountService;
    private final TransactionMapper mapper;
    private final MessageSender messageSender;

    public CreateTransactionResponseDto initTransaction(CreateTransactionRequestDto requestDto) {
        accountService.checkAndBlockBalance(requestDto.getSender().getCif(), requestDto.getSenderRequisite().getIban(), requestDto.getCurrency(), requestDto.getAmount());
        TransactionEntity transaction = mapper.toTransactionEntity(requestDto);
        repository.save(transaction);
        return new CreateTransactionResponseDto(transaction.getId());
    }

    public void sendTransactionToQueue(String transactionId) {
        messageSender.sendTransaction(transactionId);
    }
}
