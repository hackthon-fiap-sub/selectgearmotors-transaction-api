package br.com.selectgearmotors.transaction.core.ports.in.transactiontype;

import br.com.selectgearmotors.transaction.core.domain.TransactionType;

import java.util.List;

public interface FindTransactionTypesPort {
    List<TransactionType> findAll();
}
