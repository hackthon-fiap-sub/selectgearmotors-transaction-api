package br.com.selectgearmotors.transaction.core.ports.in.transaction;

import br.com.selectgearmotors.transaction.core.domain.Transaction;

import java.util.List;

public interface FindTransactionsPort {
    List<Transaction> findAll();
}
