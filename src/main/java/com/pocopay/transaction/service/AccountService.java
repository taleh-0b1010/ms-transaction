package com.pocopay.transaction.service;

import com.pocopay.transaction.dao.AccountRepository;
import com.pocopay.transaction.dao.entity.AccountEntity;
import com.pocopay.transaction.error.AccountBlockedException;
import com.pocopay.transaction.error.BadRequestException;
import com.pocopay.transaction.error.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    public AccountEntity getAccountByIban(String iban) {
        return repository.findByIban(iban)
                .orElseThrow(() -> new NotFoundException("Receiver account not found by iban"));
    }

    @Transactional
    public void checkAndBlockBalance(String cif, String iban, String currency, BigDecimal transactionAmount) { //FIXME- Provide concurrent access to an account
        repository.findByCifAndIbanAndCurrency(cif, iban, currency)
                .filter(entity -> {
                    checkBlock(entity.getBlockOut(), "debit");
                    checkAvailableBalance(entity.getAvailableBalance(), transactionAmount);
                    return true;
                })
                .ifPresentOrElse(
                        entity -> {
                            BigDecimal availableBalance = entity.getAvailableBalance();
                            BigDecimal availableBalanceNew = availableBalance.subtract(transactionAmount);
                            entity.setAvailableBalance(availableBalanceNew);
                            repository.save(entity);
                        },
                        () -> { throw new NotFoundException("Sender account not found by customerId and iban"); });
    }

    private void checkBlock(Boolean block, String s) {
        if (block) {
            throw new BadRequestException("Account is blocked for " + s);
        }
    }

    private void checkAvailableBalance(BigDecimal availableBalance, BigDecimal transactionAmount) {
        if (availableBalance.subtract(transactionAmount).signum() < 0) {
            throw new BadRequestException("Account does not have sufficient balance");
        }
    }
}
