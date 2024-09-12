package br.com.selectgearmotors.transaction.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private BigDecimal transactionAmount;
    private String clientId;
    private String transactionId;
    private String personType;
}