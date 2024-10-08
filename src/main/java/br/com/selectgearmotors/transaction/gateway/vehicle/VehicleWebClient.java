package br.com.selectgearmotors.transaction.gateway.vehicle;

import br.com.selectgearmotors.transaction.gateway.dto.VehicleResponseDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class VehicleWebClient {

    @Value("${gateway.vehicle.url}")
    private String baseUrl;

    private WebClient webClient;

    public VehicleWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @PostConstruct
    private void init() {
        this.webClient = this.webClient.mutate().baseUrl(baseUrl).build();
    }

    public VehicleResponseDTO get(String clientCode) {
        return webClient.get()
                .uri("/vehicles/code/{clientCode}", clientCode)
                .retrieve()
                .bodyToMono(VehicleResponseDTO.class)
                .block();
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
