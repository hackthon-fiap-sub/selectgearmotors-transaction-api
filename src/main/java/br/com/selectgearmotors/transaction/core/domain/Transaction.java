package br.com.selectgearmotors.transaction.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Transaction", requiredProperties = {"id, code, vehicleCode, clientCode, price, transactionStatus, transactionTypeId"})
@Tag(name = "Transaction", description = "Model")
public class Transaction implements Serializable {

    @Schema(description = "Unique identifier of the Transaction.",
            example = "1")
    private Long id;

    @Schema(description = "name of the Transaction.",
            example = "V$")
    @NotNull(message = "o campo \"code\" é obrigario")
    @Size(min = 3, max = 255)
    private String code;

    @Schema(description = "name of the Transaction.",
            example = "V$")
    private String vehicleCode;

    @Schema(description = "name of the Transaction.",
            example = "V$")
    private String clientCode;

    @Schema(description = "Description of the Transaction.",
            example = "Coca-cola !L")
    private String carSellerCode;

    @Schema(description = "price of the Transaction.",
            example = "V$")
    @NotNull(message = "o campo \"price\" é obrigario")
    private BigDecimal price;

    @Schema(description = "Restaurant of the User.",
            example = "1", ref = "TransactionStatus")
    @NotNull
    private String transactionStatus;

    @Schema(description = "Restaurant of the User.",
            example = "1", ref = "TransactionTypeEntity")
    @NotNull
    private Long transactionTypeId;

    private String personType;

    public void update(Long id, Transaction transaction) {
        this.id = id;
        this.code = transaction.getCode();
        this.vehicleCode = transaction.getVehicleCode();
        this.clientCode = transaction.getClientCode();
        this.price = transaction.getPrice();
        this.transactionStatus = transaction.getTransactionStatus();
        this.transactionTypeId = transaction.getTransactionTypeId();
        this.personType = transaction.getPersonType();
    }
}
