package br.com.selectgearmotors.transaction.application.api.mapper;

import br.com.selectgearmotors.transaction.application.api.dto.request.TransactionRequest;
import br.com.selectgearmotors.transaction.application.api.dto.response.TransactionResponse;
import br.com.selectgearmotors.transaction.core.domain.Transaction;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionApiMapper {

    @Mapping(source = "vehicleId", target = "vehicleId")
    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "transactionStatus", target = "transactionStatus")
    @Mapping(source = "transactionTypeId", target = "transactionTypeId")
    Transaction fromRequest(TransactionRequest request);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    TransactionResponse fromEntity(Transaction transaction);

   List<TransactionResponse> map(List<Transaction> transactions);

}