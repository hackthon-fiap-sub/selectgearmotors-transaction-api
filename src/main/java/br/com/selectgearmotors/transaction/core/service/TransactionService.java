package br.com.selectgearmotors.transaction.core.service;

import br.com.selectgearmotors.transaction.application.api.exception.ResourceFoundException;
import br.com.selectgearmotors.transaction.core.domain.Transaction;
import br.com.selectgearmotors.transaction.core.ports.in.transaction.*;
import br.com.selectgearmotors.transaction.core.ports.out.TransactionRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TransactionService implements CreateTransactionPort, UpdateTransactionPort, FindByIdTransactionPort, FindTransactionsPort, DeleteTransactionPort {

    private final TransactionRepositoryPort transactionRepository;

    @Autowired
    public TransactionService(TransactionRepositoryPort transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction update(Long id, Transaction transaction) {
        Transaction resultById = findById(id);
        if (resultById != null) {
            resultById.update(id, transaction);

            return transactionRepository.save(resultById);
        }

        return null;
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Transaction findByCode(String code) {
        return transactionRepository.findByCode(code);
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public boolean remove(Long id) {
        try {
            Transaction transactionById = findById(id);
            if (transactionById == null) {
                throw new ResourceFoundException("Transaction not found");
            }

            transactionRepository.remove(id);
            return Boolean.TRUE;
        } catch (ResourceFoundException e) {
            log.error("Erro ao remover produto: {}", e.getMessage());
            return Boolean.FALSE;
        }
    }
}
