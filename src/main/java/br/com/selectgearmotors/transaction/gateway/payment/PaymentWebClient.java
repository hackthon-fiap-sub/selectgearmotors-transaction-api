package br.com.selectgearmotors.transaction.gateway.payment;

import br.com.selectgearmotors.transaction.gateway.dto.PaymentDto;
import br.com.selectgearmotors.transaction.gateway.dto.PaymentResponseDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Component
public class PaymentWebClient {

    @Value("${app.payment-rest.url}")
    private String url;

    private final WebClient.Builder webClientBuilder;
    private WebClient webClient;

    public PaymentWebClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @PostConstruct
    private void init() {
        this.webClient = webClientBuilder.baseUrl(url).build();
    }

    public PaymentResponseDto setPayment(PaymentDto paymentDto) {
        try {
            log.info("Sending payment request: {}", paymentDto);
            return webClient.post()
                    .uri("/payments")
                    .bodyValue(paymentDto)
                    .retrieve()
                    .bodyToMono(PaymentResponseDto.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error during web client call: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    private String getUrl() {
        return url;
    }
}
