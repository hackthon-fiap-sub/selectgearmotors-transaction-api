package br.com.selectgearmotors.transaction.application.database.mapper;

import br.com.selectgearmotors.transaction.core.domain.TransactionType;
import br.com.selectgearmotors.transaction.infrastructure.entity.transactiontype.TransactionTypeEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionTypeMapper {

    @Mapping(source = "name", target = "name")
    TransactionTypeEntity fromModelTpEntity(TransactionType productCategory);
    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    TransactionType fromEntityToModel(TransactionTypeEntity productCategoryEntity);

    List<TransactionType> map(List<TransactionTypeEntity> productCategoryEntities);
}
