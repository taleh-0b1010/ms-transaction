package com.pocopay.transaction.dao.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Table(name = "account")
public class AccountEntity {

    @Id
    private Long id;
    private String iban;
    private Long accountNumber;
    private String currency;
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private String customerId;
    private Boolean blockIn;
    private Boolean blockOut;
    private Boolean closed;
}
