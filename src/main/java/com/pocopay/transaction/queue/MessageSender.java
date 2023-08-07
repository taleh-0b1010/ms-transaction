package com.pocopay.transaction.queue;

import com.pocopay.transaction.dao.entity.TransactionEntity;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    private final Queue transactionQueue;
    private final RabbitTemplate template;

    public MessageSender(@Qualifier("transaction") Queue transactionQueue,
                         RabbitTemplate template) {
        this.transactionQueue = transactionQueue;
        this.template = template;
    }

    public void sendTransactionToQueue(TransactionEntity entity) {
        template.convertAndSend(transactionQueue.getName(), entity);
    }
}
