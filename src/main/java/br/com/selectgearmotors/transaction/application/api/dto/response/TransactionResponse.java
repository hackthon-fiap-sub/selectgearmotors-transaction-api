package br.com.selectgearmotors.transaction.application.api.dto.response;

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
@Schema(description = "TransactionResponse", requiredProperties = {"id", "name", "description", "price", "pic", "productCategoryId", "restaurantId"})
@Tag(name = "TransactionResponse", description = "Model")
public class TransactionResponse implements Serializable {

    @Schema(description = "Unique identifier of the Driver.",
            example = "1")
    private Long id;

    @Schema(description = "Name of the Transaction.",
            example = "Coca-cola")
    private String vehicleCode;

    @Schema(description = "Description of the Transaction.",
            example = "Coca-cola !L")
    private String clientCode;

    private String code;

    @Schema(description = "Price of the Transaction.",
            example = "9.00")
    private BigDecimal price;

    private String transactionStatus;

    @Schema(description = "Transaction Category of the Transaction.",
            example = "Bebida", ref = "TransactionType")
    private Long transactionTypeId;

}
