package br.com.selectgearmotors.transaction.core.service;

import br.com.selectgearmotors.transaction.application.api.exception.ResourceFoundException;
import br.com.selectgearmotors.transaction.core.domain.TransactionType;
import br.com.selectgearmotors.transaction.core.ports.in.transactiontype.*;
import br.com.selectgearmotors.transaction.core.ports.out.TransactionTypeRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TransactionTypeService implements CreateTransactionTypePort, UpdateTransactionTypePort, FindByIdTransactionTypePort, FindTransactionTypesPort, DeleteTransactionTypePort {

    private final TransactionTypeRepositoryPort transactionTypeRepositoryPort;

    @Autowired
    public TransactionTypeService(TransactionTypeRepositoryPort transactionTypeRepositoryPort) {
        this.transactionTypeRepositoryPort = transactionTypeRepositoryPort;
    }

    @Override
    public TransactionType save(TransactionType product) {
        return transactionTypeRepositoryPort.save(product);
    }

    @Override
    public TransactionType update(Long id, TransactionType product) {
        TransactionType resultById = findById(id);
        if (resultById != null) {
            resultById.update(id, product);

            return transactionTypeRepositoryPort.save(resultById);
        }

        return null;
    }

    @Override
    public TransactionType findById(Long id) {
        return transactionTypeRepositoryPort.findById(id);
    }

    @Override
    public List<TransactionType> findAll() {
       return transactionTypeRepositoryPort.findAll();
    }

    @Override
    public boolean remove(Long id) {
        try {
            TransactionType productCategoryById = findById(id);
            if (productCategoryById == null) {
                throw new ResourceFoundException("Transaction Category not found");
            }

            transactionTypeRepositoryPort.remove(id);
            return Boolean.TRUE;
        } catch (ResourceFoundException e) {
            log.error("Erro ao remover produto: {}", e.getMessage());
            return Boolean.FALSE;
        }
    }
}
