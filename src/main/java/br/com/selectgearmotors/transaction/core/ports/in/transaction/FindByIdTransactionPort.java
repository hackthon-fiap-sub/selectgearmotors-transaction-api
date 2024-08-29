package br.com.selectgearmotors.transaction.core.ports.in.transaction;

import br.com.selectgearmotors.transaction.core.domain.Transaction;

public interface FindByIdTransactionPort {
    Transaction findById(Long id);
    Transaction findByCode(String code);
}
