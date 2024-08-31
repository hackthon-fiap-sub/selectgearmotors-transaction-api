package br.com.selectgearmotors.transaction.core.ports.out;

import br.com.selectgearmotors.transaction.core.domain.Transaction;

import java.util.List;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);
    boolean remove(Long id);
    Transaction findById(Long id);
    List<Transaction> findAll();
    Transaction update(Long id, Transaction transaction);
    Transaction findByCode(String code);
    Transaction findByVehicleCode(String vehicleCode);
}
