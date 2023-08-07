package com.pocopay.transaction.service;

import com.pocopay.transaction.dao.TransactionRepository;
import com.pocopay.transaction.dao.entity.AccountEntity;
import com.pocopay.transaction.dao.entity.TransactionEntity;
import com.pocopay.transaction.error.NotFoundException;
import com.pocopay.transaction.mapper.TransactionMapper;
import com.pocopay.transaction.model.CreateTransactionRequestDto;
import com.pocopay.transaction.model.CreateTransactionResponseDto;
import com.pocopay.transaction.model.TransactionDto;
import com.pocopay.transaction.model.TransactionStatus;
import com.pocopay.transaction.queue.MessageSender;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final AccountService accountService;
    private final TransactionMapper mapper;
    private final MessageSender messageSender;
    private final RateService rateService;

    public TransactionService(TransactionRepository repository, AccountService accountService, TransactionMapper mapper, MessageSender messageSender, RateService rateService) {
        this.repository = repository;
        this.accountService = accountService;
        this.mapper = mapper;
        this.messageSender = messageSender;
        this.rateService = rateService;
    }

    public CreateTransactionResponseDto initTransaction(CreateTransactionRequestDto requestDto) {
        accountService.checkAndBlockBalance(requestDto.sender().cif(), requestDto.senderRequisite().iban(), requestDto.currency(), requestDto.amount());
        TransactionEntity transaction = mapper.toTransactionEntity(requestDto);
        repository.save(transaction);
        return new CreateTransactionResponseDto(transaction.getId());
    }

    public void executeTransaction(String cif, String transactionId) {
        repository.findByCifAndId(cif, transactionId)
                .ifPresentOrElse(
                        messageSender::sendTransactionToQueue,
                        () -> {throw new NotFoundException("Transaction not found");}
                );
    }

    @Transactional
    public void executeTransaction(TransactionEntity transactionEntity) throws InterruptedException {
        BigDecimal amount = transactionEntity.getAmount();
        Optional<AccountEntity> receiverAccount = accountService.getAndCheckAccount(transactionEntity.getReceiverIban());
        receiverAccount.ifPresentOrElse(
                entity -> {
                    BigDecimal rate = rateService.getRate(transactionEntity.getCurrency(), entity.getCurrency());
                    BigDecimal amountAfterRate = amount.multiply(rate);

                    BigDecimal receiverAvailableBalance = entity.getAvailableBalance();
                    BigDecimal receiverAvailableBalanceNew = receiverAvailableBalance.add(amountAfterRate);
                    entity.setAvailableBalance(receiverAvailableBalanceNew);

                    BigDecimal receiverBalance = entity.getBalance();
                    BigDecimal receiverBalanceNew = receiverBalance.add(amountAfterRate);
                    entity.setBalance(receiverBalanceNew);
                    accountService.updateAccount(entity);
                },
                () -> {
                    transactionEntity.setStatus(TransactionStatus.REJECTED);
                    transactionEntity.setError("Receiver account is not found");
                    repository.save(transactionEntity);
                }
        );

        if (isTransactionRejected(transactionEntity.getStatus())) return;

        Optional<AccountEntity> senderAccount = accountService.getSenderAccount(transactionEntity.getCif(), transactionEntity.getSenderIban(), transactionEntity.getCurrency());
        senderAccount.ifPresentOrElse(
                entity -> {
                    BigDecimal senderBalance = entity.getBalance();
                    BigDecimal senderBalanceNew = senderBalance.subtract(amount);
                    entity.setBalance(senderBalanceNew);
                    accountService.updateAccount(entity);
                },
                () -> {
                    transactionEntity.setStatus(TransactionStatus.REJECTED);
                    transactionEntity.setError("Sender account is not found");
                    repository.save(transactionEntity);
                }
        );

        if (isTransactionRejected(transactionEntity.getStatus())) return;

        transactionEntity.setStatus(TransactionStatus.IN_PROGRESS);
        repository.save(transactionEntity);

        //Simulate real transaction
        Thread.sleep(8000);

        transactionEntity.setStatus(TransactionStatus.SUCCEEDED);
        repository.save(transactionEntity);
    }

    public boolean isTransactionRejected(TransactionStatus status) {
        return status == TransactionStatus.REJECTED;
    }

    public TransactionDto getTransaction(String cif, String transactionId) {
        return repository.findByCifAndId(cif, transactionId)
                .map(mapper::toTransactionDto)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));
    }
}
