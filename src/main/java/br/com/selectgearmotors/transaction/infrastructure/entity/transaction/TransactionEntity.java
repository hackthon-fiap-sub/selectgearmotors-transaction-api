package br.com.selectgearmotors.transaction.infrastructure.entity.transaction;

import br.com.selectgearmotors.transaction.infrastructure.entity.domain.AuditDomain;
import br.com.selectgearmotors.transaction.infrastructure.entity.domain.TransactionStatus;
import br.com.selectgearmotors.transaction.infrastructure.entity.transactiontype.TransactionTypeEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_transaction", schema = "transactions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "TransactionEntity", requiredProperties = {"id, code, name, price, productCategory, restaurant"})
@Tag(name = "TransactionEntity", description = "Model")
public class TransactionEntity extends AuditDomain {

    @Schema(description = "Unique identifier of the Transaction.",
            example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Schema(description = "name of the Transaction.",
            example = "V$")
    @NotNull(message = "o campo \"code\" é obrigario")
    @Size(min = 3, max = 255)
    @Column(name = "code", length = 255)
    private String code;

    @Schema(description = "name of the Transaction.",
            example = "V$")
    @Column(name = "vehicle_code")
    private String vehicleCode;

    @Schema(description = "name of the Transaction.",
            example = "V$")
    @Column(name = "client_code")
    private String clientCode;

    @Schema(description = "name of the Transaction.",
            example = "V$")
    @Column(name = "car_seller_code")
    private String carSellerCode;

    @Schema(description = "price of the Transaction.",
            example = "V$")
    @NotNull(message = "o campo \"price\" é obrigario")
    private BigDecimal price;

    @Schema(description = "Restaurant of the User.",
            example = "1", ref = "TransactionStatus")
    @NotNull
    @Column(name = "transaction_status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Schema(description = "Restaurant of the User.",
            example = "1", ref = "TransactionTypeEntity")
    @NotNull
    @ManyToOne
    @JoinColumn(name = "transaction_type_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private TransactionTypeEntity transactionType;

    public void update(Long id, TransactionEntity transactionEntity) {
        this.id = id;
        this.code = transactionEntity.getCode();
        this.vehicleCode = transactionEntity.getVehicleCode();
        this.clientCode = transactionEntity.getClientCode();
        this.carSellerCode = transactionEntity.getCarSellerCode();
        this.price = transactionEntity.getPrice();
        this.transactionStatus = transactionEntity.getTransactionStatus();
        this.transactionType = transactionEntity.getTransactionType();
    }
}
