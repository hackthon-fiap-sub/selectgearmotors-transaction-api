package br.com.selectgearmotors.transaction.application.api.mapper;

import br.com.selectgearmotors.transaction.application.api.dto.request.TransactionTypeRequest;
import br.com.selectgearmotors.transaction.application.api.dto.response.TransactionTypeResponse;
import br.com.selectgearmotors.transaction.core.domain.TransactionType;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionTypeApiMapper {

    @Mapping(source = "name", target = "name")
    TransactionType fromRequest(TransactionTypeRequest request);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    TransactionTypeResponse fromEntity(TransactionType productCategory);

   List<TransactionTypeResponse> map(List<TransactionType> productCategories);
}
