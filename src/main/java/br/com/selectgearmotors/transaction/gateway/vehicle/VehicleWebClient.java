package br.com.selectgearmotors.transaction.gateway.vehicle;

import br.com.selectgearmotors.transaction.gateway.dto.VehicleDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class VehicleWebClient {

    private final WebClient webClient;

    public VehicleWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9914/api/v1").build();
    }

    public VehicleDTO get(String clientId) {
        return webClient.get()
                .uri("/clients/code/{clientId}", clientId)
                .retrieve()
                .bodyToMono(VehicleDTO.class)
                .block();
    }
}
