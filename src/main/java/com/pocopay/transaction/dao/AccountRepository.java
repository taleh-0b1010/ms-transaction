package com.pocopay.transaction.dao;

import com.pocopay.transaction.dao.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByCifAndIbanAndCurrency(String cif, String iban, String currency);

    Optional<AccountEntity> findByIban(String iban);
}
