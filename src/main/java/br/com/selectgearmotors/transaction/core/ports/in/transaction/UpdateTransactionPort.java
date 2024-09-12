package br.com.selectgearmotors.transaction.core.ports.in.transaction;

import br.com.selectgearmotors.transaction.core.domain.Transaction;

public interface UpdateTransactionPort {
    Transaction update(Long id, Transaction transaction);
    Transaction updateStatus(String transactionId, String status);
}
