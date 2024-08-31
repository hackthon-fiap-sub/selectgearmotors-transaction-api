package br.com.selectgearmotors.transaction.application.database.repository;

import br.com.selectgearmotors.transaction.application.database.mapper.TransactionMapper;
import br.com.selectgearmotors.transaction.core.domain.Transaction;
import br.com.selectgearmotors.transaction.core.ports.out.TransactionRepositoryPort;
import br.com.selectgearmotors.transaction.infrastructure.entity.transaction.TransactionEntity;
import br.com.selectgearmotors.transaction.infrastructure.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionRepositoryAdapter(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public Transaction save(Transaction transaction) {
        try {
            TransactionEntity transactionEntity = transactionMapper.fromModelTpEntity(transaction);
            if (transactionEntity != null) {
                transactionEntity.setCode(UUID.randomUUID().toString());
                TransactionEntity saved = transactionRepository.save(transactionEntity);
                return transactionMapper.fromEntityToModel(saved);
            }
        } catch (Exception e) {
            log.info("Erro ao salvar produto: " + e.getMessage());
            return null;
        }

        return null;
    }

    @Override
    public boolean remove(Long id) {
        try {
            transactionRepository.deleteById(id);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public Transaction findById(Long id) {
        Optional<TransactionEntity> buTransaction = transactionRepository.findById(id);
        if (buTransaction.isPresent()) {
            return transactionMapper.fromEntityToModel(buTransaction.get());
        }
        return null;
    }

    @Override
    public List<Transaction> findAll() {
        List<TransactionEntity> all = transactionRepository.findAll();
        return transactionMapper.map(all);
    }

    @Override
    public Transaction update(Long id, Transaction transaction) {
        Optional<TransactionEntity> resultById = transactionRepository.findById(id);
        if (resultById.isPresent()) {

            TransactionEntity transactionToChange = resultById.get();
            transactionToChange.update(id, transactionToChange);

            return transactionMapper.fromEntityToModel(transactionRepository.save(transactionToChange));
        }

        return null;
    }

    @Override
    public Transaction findByCode(String code) {
        TransactionEntity byCode = transactionRepository.findByCode(code);
        return transactionMapper.fromEntityToModel(byCode);
    }

    @Override
    public Transaction findByVehicleCode(String vehicleCode) {
        TransactionEntity byCode = transactionRepository.findByVehicleCode(vehicleCode);
        return transactionMapper.fromEntityToModel(byCode);
    }
}
