package com.pocopay.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateTransactionResponseDto {

    private String transactionId;
}
