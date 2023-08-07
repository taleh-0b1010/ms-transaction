package com.pocopay.transaction.service;

import com.pocopay.transaction.dao.RateRepository;
import com.pocopay.transaction.dao.entity.RateEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RateService {

    private final RateRepository repository;

    public BigDecimal getRate(String sourceCcy, String destCcy) {
        return repository.findBySourceCurrencyAndDestinationCurrency(sourceCcy, destCcy)
                .map(RateEntity::getRate)
                .orElse(new BigDecimal(0));
    }
}
