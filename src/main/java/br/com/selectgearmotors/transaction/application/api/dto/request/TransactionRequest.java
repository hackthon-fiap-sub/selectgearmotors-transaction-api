package br.com.selectgearmotors.transaction.application.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Schema(description = "TransactionRequest", requiredProperties = {"id", "name", "description", "price", "pic", "productCategoryId", "restaurantId"})
@Tag(name = "TransactionRequest", description = "Model")
public class TransactionRequest implements Serializable {

    @Schema(description = "Unique identifier of the Driver.",
            example = "1")
    private Long id;

    @Schema(description = "Name of the Transaction.",
            example = "Coca-cola")
    private String vehicleCode;

    @Schema(description = "Description of the Transaction.",
            example = "Coca-cola !L")
    private String clientCode;

    @Schema(description = "Price of the Transaction.",
            example = "9.00")
    private BigDecimal price;

    private String transactionStatus;

    @Schema(description = "Transaction Category of the Transaction.",
            example = "Bebida", ref = "TransactionType")
    private Long transactionTypeId;
}
