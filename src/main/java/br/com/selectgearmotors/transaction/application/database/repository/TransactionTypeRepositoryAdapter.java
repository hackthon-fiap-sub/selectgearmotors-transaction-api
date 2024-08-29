package br.com.selectgearmotors.transaction.application.database.repository;

import br.com.selectgearmotors.transaction.application.api.exception.ResourceFoundException;
import br.com.selectgearmotors.transaction.application.database.mapper.TransactionTypeMapper;
import br.com.selectgearmotors.transaction.commons.exception.ResourceNotRemoveException;
import br.com.selectgearmotors.transaction.core.domain.TransactionType;
import br.com.selectgearmotors.transaction.core.ports.out.TransactionTypeRepositoryPort;
import br.com.selectgearmotors.transaction.infrastructure.entity.transactiontype.TransactionTypeEntity;
import br.com.selectgearmotors.transaction.infrastructure.repository.TransactionTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class TransactionTypeRepositoryAdapter implements TransactionTypeRepositoryPort {

    private final TransactionTypeRepository transactionTypeRepository;
    private final TransactionTypeMapper transactionTypeMapper;

    @Autowired
    public TransactionTypeRepositoryAdapter(TransactionTypeRepository transactionTypeRepository, TransactionTypeMapper transactionTypeMapper) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.transactionTypeMapper = transactionTypeMapper;
    }

    @Override
    public TransactionType save(TransactionType productCategory) {
        try {
            TransactionTypeEntity productCategoryEntity = transactionTypeMapper.fromModelTpEntity(productCategory);
            TransactionTypeEntity saved = transactionTypeRepository.save(productCategoryEntity);
            validateSavedEntity(saved);
            return transactionTypeMapper.fromEntityToModel(saved);
        } catch (ResourceFoundException e) {
            log.error("Erro ao salvar produto: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean remove(Long id) {
         try {
             transactionTypeRepository.deleteById(id);
            return Boolean.TRUE;
        } catch (ResourceNotRemoveException e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public TransactionType findById(Long id) {
        Optional<TransactionTypeEntity> buTransactionType = transactionTypeRepository.findById(id);
        if (buTransactionType.isPresent()) {
            return transactionTypeMapper.fromEntityToModel(buTransactionType.get());
        }
        return null;
    }

    @Override
    public List<TransactionType> findAll() {
        List<TransactionTypeEntity> all = transactionTypeRepository.findAll();
        return transactionTypeMapper.map(all);
    }

    @Override
    public TransactionType update(Long id, TransactionType productCategory) {
        Optional<TransactionTypeEntity> resultById = transactionTypeRepository.findById(id);
        if (resultById.isPresent()) {
            TransactionTypeEntity productCategoryToChange = resultById.get();
            productCategoryToChange.update(id, productCategoryToChange);

            return transactionTypeMapper.fromEntityToModel(transactionTypeRepository.save(productCategoryToChange));
        }
        return null;
    }

    private void validateSavedEntity(TransactionTypeEntity saved) {
        if (saved == null) {
            throw new ResourceFoundException("Erro ao salvar produto no repositorio: entidade salva é nula");
        }

        if (saved.getName() == null) {
            throw new ResourceFoundException("Erro ao salvar produto no repositorio: nome do produto é nulo");
        }
    }
}
