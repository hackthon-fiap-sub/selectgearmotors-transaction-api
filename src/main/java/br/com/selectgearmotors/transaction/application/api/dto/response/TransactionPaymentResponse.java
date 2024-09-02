package br.com.selectgearmotors.transaction.application.api.dto.response;

import br.com.selectgearmotors.transaction.gateway.dto.PaymentResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "TransactionResponse", requiredProperties = {"id", "name", "description", "price", "pic", "productCategoryId", "restaurantId"})
@Tag(name = "TransactionResponse", description = "Model")
public class TransactionPaymentResponse {
    private TransactionResponse transactionData;
    private PaymentResponseDto paymentData;
}
