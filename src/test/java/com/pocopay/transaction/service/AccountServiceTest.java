package com.pocopay.transaction.service;

import com.pocopay.transaction.dao.AccountRepository;
import com.pocopay.transaction.dao.entity.AccountEntity;
import com.pocopay.transaction.error.BadRequestException;
import com.pocopay.transaction.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountRepository repository;

    @InjectMocks
    AccountService service;

    @Test
    public void checkAndBlockBalance_success() {
        //given
        String cif = "0001";
        String iban = "EE00000000";
        String currency = "EUR";
        BigDecimal transactionAmount = new BigDecimal(50);

        AccountEntity entity = AccountEntity.builder()
                .iban(iban)
                .cif(cif)
                .currency(currency)
                .balance(new BigDecimal(100))
                .availableBalance(new BigDecimal(90))
                .blockIn(false)
                .blockOut(false).build();

        assertAll(
                () -> {
                    //given
                    given(repository.findByCifAndIbanAndCurrency(cif, iban, currency)).willReturn(Optional.of(entity));
                    //when
                    service.checkAndBlockBalance(cif, iban, currency, transactionAmount);
                    //then
                    assertThat(entity.getAvailableBalance()).isEqualTo(new BigDecimal(40));
                    then(repository).should(times(1)).save(any(AccountEntity.class));
                },
                () -> {
                    given(repository.findByCifAndIbanAndCurrency(cif, iban, currency)).willReturn(Optional.of(entity));
                    BadRequestException exception = assertThrows(BadRequestException.class,
                            () -> service.checkAndBlockBalance(cif, iban, currency, new BigDecimal(200)));
                    assertThat(exception.getMessage()).isEqualTo("Account does not have sufficient balance");
                },
                () -> assertThrows(NotFoundException.class,
                        () -> {
                            //given
                            given(repository.findByCifAndIbanAndCurrency(cif, iban, currency)).willReturn(Optional.empty());
                            //when
                            service.checkAndBlockBalance(cif, iban, currency, transactionAmount);
                            //then
                            then(repository).shouldHaveNoInteractions();
                        }),
                () -> assertThrows(BadRequestException.class,
                        () -> {
                            //given
                            AccountEntity account = AccountEntity.builder()
                                    .blockIn(false)
                                    .blockOut(true).build();
                            given(repository.findByCifAndIbanAndCurrency(cif, iban, currency)).willReturn(Optional.of(account));
                            //when
                            service.checkAndBlockBalance(cif, iban, currency, transactionAmount);
                            //then
                            then(repository).shouldHaveNoInteractions();
                        })
        );
    }

    @Test
    public void getAndCheckAccount() {
        assertAll(
                () -> {
                    //given
                    String iban = "EE00000000";
                    AccountEntity entity = AccountEntity.builder()
                            .iban(iban)
                            .blockIn(false)
                            .blockOut(false).build();
                    given(repository.findByIban(iban)).willReturn(Optional.of(entity));

                    //when
                    Optional<AccountEntity> account = service.getAndCheckAccount(iban);

                    //then
                    assertThat(account).isPresent();
                    assertThat(account.get().getIban()).isEqualTo(iban);
                },
                () -> {
                    String iban = "EE00000000";
                    AccountEntity entity = AccountEntity.builder()
                            .iban(iban)
                            .blockIn(true)
                            .blockOut(false).build();
                    given(repository.findByIban(iban)).willReturn(Optional.of(entity));
                    BadRequestException exception = assertThrows(BadRequestException.class,
                            () -> service.getAndCheckAccount(iban));
                    assertThat(exception.getMessage()).isEqualTo("Account is blocked for credit");
                }
        );
    }
}