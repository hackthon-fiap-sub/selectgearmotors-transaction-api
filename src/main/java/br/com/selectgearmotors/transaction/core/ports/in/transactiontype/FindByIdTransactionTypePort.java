package br.com.selectgearmotors.transaction.core.ports.in.transactiontype;

import br.com.selectgearmotors.transaction.core.domain.TransactionType;

public interface FindByIdTransactionTypePort {
    TransactionType findById(Long id);
}
