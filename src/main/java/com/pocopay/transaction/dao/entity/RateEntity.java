package com.pocopay.transaction.dao.entity;

import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Table(name = "rate")
public class RateEntity {

    private Long id;
    private String sourceCurrency;
    private String destinationCurrency;
    private BigDecimal rate;
}
