package com.pocopay.transaction.model;

import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

public record TransactionDto(
        String id,
        String cif,
        String currency,
        BigDecimal amount,
        String senderIban,
        String receiverIban,
        String senderFirstName,
        String senderLastName,
        String receiverFirstName,
        String receiverLastName,
        TransactionStatus status,
        String error
) {
}
