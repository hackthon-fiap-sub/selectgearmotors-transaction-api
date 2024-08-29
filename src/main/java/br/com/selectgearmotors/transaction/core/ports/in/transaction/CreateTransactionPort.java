package br.com.selectgearmotors.transaction.core.ports.in.transaction;

import br.com.selectgearmotors.transaction.core.domain.Transaction;

public interface CreateTransactionPort {
    Transaction save(Transaction transaction);
}
