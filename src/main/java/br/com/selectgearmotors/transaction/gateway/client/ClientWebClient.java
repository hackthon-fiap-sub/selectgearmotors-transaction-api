package br.com.selectgearmotors.transaction.gateway.client;

import br.com.selectgearmotors.transaction.gateway.dto.ClientDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ClientWebClient {

    private final WebClient webClient;

    public ClientWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9914/api/v1").build();
    }

    public ClientDTO get(String clientId) {
        return webClient.get()
                .uri("/clients/code/{clientId}", clientId)
                .retrieve()
                .bodyToMono(ClientDTO.class)
                .block();
    }
}
