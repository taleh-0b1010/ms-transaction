package com.pocopay.transaction.dao.entity;

import com.pocopay.transaction.model.TransactionStatus;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    private String id;
    private String customerId;
    private String currency;
    private BigDecimal amount;
    private String senderIban;
    private String receiverIban;
    private String senderFirstName;
    private String senderLastName;
    private String receiverFirstName;
    private String receiverLastName;
    private TransactionStatus status;
    private String error;
    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime updated;
}
