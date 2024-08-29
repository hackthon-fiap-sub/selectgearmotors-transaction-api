package br.com.selectgearmotors.transaction.core.ports.in.transactiontype;

import br.com.selectgearmotors.transaction.core.domain.TransactionType;

public interface UpdateTransactionTypePort {
    TransactionType update(Long id, TransactionType transactionType);
}
