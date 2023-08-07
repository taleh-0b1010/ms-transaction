package com.pocopay.transaction.queue;

import com.pocopay.transaction.dao.entity.TransactionEntity;
import com.pocopay.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageReceiver {

    private final TransactionService transactionService;

    @SneakyThrows
    @RabbitListener(queues = "${spring.rabbitmq.queue.transaction}")
    public void receiverTransaction(TransactionEntity entity) {
        transactionService.executeTransaction(entity);
    }
}
