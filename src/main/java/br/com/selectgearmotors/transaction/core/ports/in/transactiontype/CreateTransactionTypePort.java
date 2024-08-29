package br.com.selectgearmotors.transaction.core.ports.in.transactiontype;

import br.com.selectgearmotors.transaction.core.domain.TransactionType;

public interface CreateTransactionTypePort {
    TransactionType save(TransactionType transactionType);
}
