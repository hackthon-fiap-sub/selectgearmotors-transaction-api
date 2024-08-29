package br.com.selectgearmotors.transaction.application.database.mapper;

import br.com.selectgearmotors.transaction.core.domain.Transaction;
import br.com.selectgearmotors.transaction.infrastructure.entity.transaction.TransactionEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "code", target = "code")
    @Mapping(source = "vehicleId", target = "vehicleId")
    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "transactionStatus", target = "transactionStatus")
    @Mapping(source = "transactionTypeId", target = "transactionType.id")
    TransactionEntity fromModelTpEntity(Transaction product);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "transactionTypeId", source = "transactionType.id")
    Transaction fromEntityToModel(TransactionEntity productEntity);

    List<Transaction> map(List<TransactionEntity> productEntities);
}