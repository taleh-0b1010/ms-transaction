package com.pocopay.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    private String id;
    private String cif;
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
}
