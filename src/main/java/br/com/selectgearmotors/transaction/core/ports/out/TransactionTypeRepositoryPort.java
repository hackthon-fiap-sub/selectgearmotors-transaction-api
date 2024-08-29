package br.com.selectgearmotors.transaction.core.ports.out;

import br.com.selectgearmotors.transaction.core.domain.TransactionType;
import java.util.List;

public interface TransactionTypeRepositoryPort {
    TransactionType save(TransactionType transactionType);
    boolean remove(Long id);
    TransactionType findById(Long id);
    List<TransactionType> findAll();
    TransactionType update(Long id, TransactionType transactionType);
}
