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
@Schema(description = "TransactionRequest", requiredProperties = {"id", "name", "description", "price", "pic", "productCategoryId", "restaurantId"})
@Tag(name = "TransactionRequest", description = "Model")
public class TransactionResponse implements Serializable {

    @Schema(description = "Unique identifier of the Driver.",
            example = "1")
    private Long id;

    @Schema(description = "Name of the Transaction.",
            example = "Vicente")
    @Size(min = 3, max = 255)
    private String name;

    @Schema(description = "Name of the Transaction.",
            example = "Vicente")
    @Size(min = 3, max = 255)
    private String code;

    @Schema(description = "Description of the Transaction.",
            example = "Vicente")
    @Size(min = 0, max = 255)
    private String description;

    @Schema(description = "value the Transaction.",
            example = "V$")
    private BigDecimal price;

    @Schema(description = "value the Transaction.",
            example = "V$")
    private String pic;

    private TransactionTypeResponse productCategory;

}
