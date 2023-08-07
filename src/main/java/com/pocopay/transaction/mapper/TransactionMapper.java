package com.pocopay.transaction.mapper;

import com.pocopay.transaction.dao.entity.TransactionEntity;
import com.pocopay.transaction.model.CreateTransactionRequestDto;
import com.pocopay.transaction.model.TransactionDto;
import com.pocopay.transaction.model.TransactionStatus;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(
        componentModel = "spring"
)
public interface TransactionMapper {

    @Mapping(target = "cif", source = "sender.cif")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "senderIban", source = "senderRequisite.iban")
    @Mapping(target = "receiverIban", source = "receiverRequisite.iban")
    @Mapping(target = "senderFirstName", source = "sender.firstName")
    @Mapping(target = "senderLastName", source = "sender.lastName")
    @Mapping(target = "receiverFirstName", source = "receiver.firstName")
    @Mapping(target = "receiverLastName", source = "receiver.lastName")
    TransactionEntity toTransactionEntity(CreateTransactionRequestDto requestDto);

    @AfterMapping
    default void afterMapping(@MappingTarget TransactionEntity entity, CreateTransactionRequestDto requestDto) {
        entity.setId(UUID.randomUUID().toString());
        entity.setStatus(TransactionStatus.INITIATED);
    }

    TransactionDto toTransactionDto(TransactionEntity entity);
}
