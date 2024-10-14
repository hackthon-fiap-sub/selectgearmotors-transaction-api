package br.com.selectgearmotors.transaction.gateway.vehicle;

import br.com.selectgearmotors.transaction.commons.filter.JwtRequestFilter;
import br.com.selectgearmotors.transaction.gateway.dto.VehicleResponseDTO;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class VehicleWebClient {

    private final HttpServletRequest request;

    @Value("${gateway.vehicle.url}")
    private String baseUrl;

    private WebClient webClient;

    public VehicleWebClient(HttpServletRequest request, WebClient.Builder webClientBuilder) {
        this.request = request;
        this.webClient = webClientBuilder.build();
    }

    @PostConstruct
    private void init() {
        this.webClient = this.webClient.mutate().baseUrl(baseUrl).build();
    }

    public VehicleResponseDTO get(String clientCode) {
        // Pega o token armazenado no filtro
        String bearerToken = (String) request.getAttribute(JwtRequestFilter.BEARER_TOKEN_ATTRIBUTE);

        return webClient.get()
                .uri("/vehicles/code/{clientCode}", clientCode)
                .headers(headers -> headers.setBearerAuth(bearerToken))
                .retrieve()
                .bodyToMono(VehicleResponseDTO.class)
                .block();
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
