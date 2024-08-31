package br.com.selectgearmotors.transaction.infrastructure.repository;

import br.com.selectgearmotors.transaction.infrastructure.entity.transaction.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    TransactionEntity findByCode(String code);
    TransactionEntity findByVehicleCode(String vehicleCode);
}
