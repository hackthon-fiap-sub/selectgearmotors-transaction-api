package br.com.selectgearmotors.transaction.gateway.company;

import br.com.selectgearmotors.transaction.gateway.dto.CarSellerResponseDTO;
import br.com.selectgearmotors.transaction.gateway.dto.ClientResponseDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CarSellerWebClient {

    @Value("${gateway.car-seller.url}")
    private String baseUrl;

    private WebClient webClient;

    public CarSellerWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @PostConstruct
    private void init() {
        this.webClient = this.webClient.mutate().baseUrl(baseUrl).build();
    }

    public CarSellerResponseDTO get(String carSellerCode) {
        return webClient.get()
                .uri("/car-sellers/code/{clientCode}", carSellerCode)
                .retrieve()
                .bodyToMono(CarSellerResponseDTO.class)
                .block();
    }
}