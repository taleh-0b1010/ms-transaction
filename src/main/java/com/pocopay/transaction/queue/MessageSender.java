package com.pocopay.transaction.queue;

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

    public void sendTransaction(String transactionId) {
        template.convertAndSend(transactionQueue.getName(), transactionId);
    }
}
