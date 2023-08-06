package com.pocopay.transaction.model;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequestDto {

    @NonNull
    private String currency;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private Customer sender;
    @NonNull
    private Customer receiver;
    @NonNull
    private Requisite senderRequisite;
    @NonNull
    private Requisite receiverRequisite;

    @Getter
    @Setter
    public static class Requisite {
        private String iban;
    }

    @Getter
    @Setter
    public static class Customer {
        private String cif;
        @NonNull
        private String firstName;
        @NonNull
        private String lastName;
    }
}
