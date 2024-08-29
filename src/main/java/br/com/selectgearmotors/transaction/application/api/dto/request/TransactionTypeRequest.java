package br.com.selectgearmotors.transaction.application.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "TransactionTypeRequest", requiredProperties = {"id", "name"})
@Tag(name = "TransactionTypeRequest", description = "Model")
public class TransactionTypeRequest implements Serializable {

    @Schema(description = "Unique identifier of the Driver.",
            example = "1")
    private Long id;

    @Schema(description = "Name of the Transaction Category.",
            example = "Bebida")
    @Size(min = 3, max = 255)
    private String name;
}
