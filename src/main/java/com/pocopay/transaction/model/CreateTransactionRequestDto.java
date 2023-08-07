package com.pocopay.transaction.model;

import lombok.NonNull;

import java.math.BigDecimal;

public record CreateTransactionRequestDto(
        @NonNull
        String currency,
        @NonNull
        BigDecimal amount,
        @NonNull
        CustomerDto sender,
        @NonNull
        CustomerDto receiver,
        @NonNull
        RequisiteDto senderRequisite,
        @NonNull
        RequisiteDto receiverRequisite
) {
}
