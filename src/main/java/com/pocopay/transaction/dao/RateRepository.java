package com.pocopay.transaction.dao;

import com.pocopay.transaction.dao.entity.RateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<RateEntity, Long> {

    Optional<RateEntity> findBySourceCurrencyAndDestinationCurrency(String sourceCcy, String destinationCcy);
}
