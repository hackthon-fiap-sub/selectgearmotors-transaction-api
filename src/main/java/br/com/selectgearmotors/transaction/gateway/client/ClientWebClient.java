package br.com.selectgearmotors.transaction.gateway.client;

import br.com.selectgearmotors.transaction.commons.Constants;
import br.com.selectgearmotors.transaction.commons.filter.JwtRequestFilter;
import br.com.selectgearmotors.transaction.gateway.dto.ClientResponseDTO;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ClientWebClient {

    private final HttpServletRequest request;

    @Value("${gateway.client.url}")
    private String baseUrl;

    private WebClient webClient;

    public ClientWebClient(HttpServletRequest request, WebClient.Builder webClientBuilder) {
        this.request = request;
        this.webClient = webClientBuilder.build();
    }

    @PostConstruct
    private void init() {
        this.webClient = this.webClient.mutate().baseUrl(baseUrl).build();
    }

    public ClientResponseDTO get(String clientCode) {
        // Pega o token armazenado no filtro
        String bearerToken = (String) request.getAttribute(Constants.BEARER_TOKEN_ATTRIBUTE);
        return webClient.get()
                .uri("/clients/code/{clientCode}", clientCode)
                .headers(headers -> headers.setBearerAuth(bearerToken))
                .retrieve()
                .bodyToMono(ClientResponseDTO.class)
                .block();
    }
}