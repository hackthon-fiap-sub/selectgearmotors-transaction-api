package br.com.selectgearmotors.transaction.infrastructure.entity.transactiontype;

import br.com.selectgearmotors.transaction.infrastructure.entity.domain.AuditDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_transaction_type", schema = "transactions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "TransactionTypeEntity", requiredProperties = {"id, name"})
@Tag(name = "TransactionTypeEntity", description = "Model")
public class TransactionTypeEntity extends AuditDomain {

    @Schema(description = "Unique identifier of the Transaction.",
            example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Schema(description = "name of the Transaction.",
            example = "V$")
    @NotNull(message = "o campo \"name\" é obrigario")
    @Size(min = 1, max = 255)
    @Column(name = "name", length = 255)
    private String name;

    public void update(Long id, TransactionTypeEntity transactionTypeEntity) {
        this.id = id;
        this.name = transactionTypeEntity.getName();
    }
}
