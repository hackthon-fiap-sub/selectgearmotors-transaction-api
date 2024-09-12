package br.com.selectgearmotors.transaction.gateway.client;

import br.com.selectgearmotors.transaction.gateway.dto.ClientResponseDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ClientWebClient {

    @Value("${gateway.client.url}")
    private String baseUrl;

    private WebClient webClient;

    public ClientWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @PostConstruct
    private void init() {
        this.webClient = this.webClient.mutate().baseUrl(baseUrl).build();
    }

    public ClientResponseDTO get(String clientCode) {
        return webClient.get()
                .uri("/clients/code/{clientCode}", clientCode)
                .retrieve()
                .bodyToMono(ClientResponseDTO.class)
                .block();
    }
}