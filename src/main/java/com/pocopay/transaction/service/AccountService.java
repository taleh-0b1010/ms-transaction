package com.pocopay.transaction.service;

import com.pocopay.transaction.dao.AccountRepository;
import com.pocopay.transaction.dao.entity.AccountEntity;
import com.pocopay.transaction.error.BadRequestException;
import com.pocopay.transaction.error.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    @Transactional
    public void checkAndBlockBalance(String cif, String iban, String currency, BigDecimal transactionAmount) { //FIXME- Provide concurrent access to an account
        repository.findByCifAndIbanAndCurrency(cif, iban, currency)
                .filter(entity -> {
                    checkBlock(entity.getBlockOut(), "debit");
                    checkAvailableBalance(entity.getAvailableBalance(), transactionAmount);
                    return true;
                })
                .ifPresentOrElse(
                        entity -> blockAvailableBalance(entity, transactionAmount),
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

    private void blockAvailableBalance(AccountEntity entity, BigDecimal transactionAmount) {
        BigDecimal availableBalance = entity.getAvailableBalance();
        BigDecimal availableBalanceNew = availableBalance.subtract(transactionAmount);
        entity.setAvailableBalance(availableBalanceNew);
        updateAccount(entity);
    }

    public Optional<AccountEntity> getAndCheckAccount(String iban) {
        return repository.findByIban(iban)
                .filter(entity -> {
                    checkBlock(entity.getBlockIn(), "credit");
                    return true;
                })
                .stream().findFirst();
    }

    public Optional<AccountEntity> getSenderAccount(String cif, String iban, String currency) {
        return repository.findByCifAndIbanAndCurrency(cif, iban, currency)
                .stream().findFirst();
    }

    public void updateAccount(AccountEntity entity) {
        repository.save(entity);
    }
}
