package br.com.selectgearmotors.transaction.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Transaction", requiredProperties = {"id, name"})
@Tag(name = "Transaction", description = "Model")
public class TransactionType implements Serializable {

    @Schema(description = "Unique identifier of the Transaction.",
            example = "1")
    private Long id;

    @Schema(description = "Unique identifier of the Transaction.",
            example = "1")
    private String name;

    public void update(Long id, TransactionType product) {
        this.id = id;
        this.name = product.getName();
    }
}
