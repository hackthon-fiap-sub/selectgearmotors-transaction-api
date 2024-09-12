package br.com.selectgearmotors.transaction.application.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Schema(description = "TransactionDTO", requiredProperties = {"id", "name", "description", "price", "pic", "productCategoryId", "restaurantId"})
@Tag(name = "TransactionDTO", description = "Model")
public class TransactionDTO implements Serializable  {

    @Schema(description = "Unique identifier of the Driver.",
            example = "1")
    private Long id;

    @Schema(description = "Name of the Transaction.",
            example = "Coca-cola")
    private Long vehicleId;

    @Schema(description = "Description of the Transaction.",
            example = "Coca-cola !L")
    private Long clientId;

    @Schema(description = "Description of the Transaction.",
            example = "Coca-cola !L")
    private Long carSellerId;

    private String code;

    @Schema(description = "Price of the Transaction.",
            example = "9.00")
    private BigDecimal price;

    private String transactionStatus;

    @Schema(description = "Transaction Category of the Transaction.",
            example = "Bebida", ref = "TransactionType")
    private Long transactionTypeId;

}
