package com.pocopay.transaction.queue;

import com.pocopay.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageReceiver {

    private final TransactionService transactionService;

    public void receiverTransaction(String transactionId) {
        transactionService.executeTransaction(transactionId);
    }
}
