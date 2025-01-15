package br.com.selectgearmotors.transaction.gateway.company;

import br.com.selectgearmotors.transaction.commons.Constants;
import br.com.selectgearmotors.transaction.commons.filter.JwtRequestFilter;
import br.com.selectgearmotors.transaction.gateway.dto.CarSellerResponseDTO;
import br.com.selectgearmotors.transaction.gateway.dto.ClientResponseDTO;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CarSellerWebClient {

    private final HttpServletRequest request;

    @Value("${gateway.car-seller.url}")
    private String baseUrl;

    private WebClient webClient;

    public CarSellerWebClient(HttpServletRequest request, WebClient.Builder webClientBuilder) {
        this.request = request;
        this.webClient = webClientBuilder.build();
    }

    @PostConstruct
    private void init() {
        this.webClient = this.webClient.mutate().baseUrl(baseUrl).build();
    }

    public CarSellerResponseDTO get(String carSellerCode) {
        // Pega o token armazenado no filtro
        //String bearerToken = (String) request.getAttribute(Constants.BEARER_TOKEN_ATTRIBUTE);
        String bearerToken = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getDetails();

        return webClient.get()
                .uri("/car-sellers/code/{clientCode}", carSellerCode)
                .headers(headers -> headers.setBearerAuth(bearerToken))
                .retrieve()
                .bodyToMono(CarSellerResponseDTO.class)
                .block();
    }
}