package br.com.selectgearmotors.transaction.infrastructure.repository;

import br.com.selectgearmotors.transaction.infrastructure.entity.transactiontype.TransactionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionTypeEntity, Long> {
    Optional<TransactionTypeEntity> findByName(String name);
}
