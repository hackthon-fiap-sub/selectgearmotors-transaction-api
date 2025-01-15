package br.com.selectgearmotors.transaction.gateway.reservation;

import br.com.selectgearmotors.transaction.commons.Constants;
import br.com.selectgearmotors.transaction.gateway.dto.ReservationResponseDTO;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ReservationWebClient {

    private final HttpServletRequest request;

    @Value("${gateway.reservation.url}")
    private String baseUrl;

    private WebClient webClient;

    public ReservationWebClient(HttpServletRequest request, WebClient.Builder webClientBuilder) {
        this.request = request;
        this.webClient = webClientBuilder.build();
    }

    @PostConstruct
    private void init() {
        this.webClient = this.webClient.mutate().baseUrl(baseUrl).build();
    }

    public ReservationResponseDTO getStatus(String vehicleCode) {
        // Pega o token armazenado no filtro
        //String bearerToken = (String) request.getAttribute(Constants.BEARER_TOKEN_ATTRIBUTE);
        String bearerToken = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getDetails();

        return webClient.get()
                .uri("/reservations/vehicle/{vehicleCode}", vehicleCode)
                .headers(headers -> headers.setBearerAuth(bearerToken))
                .retrieve()
                .bodyToMono(ReservationResponseDTO.class)
                .block();
    }

    public void setStatus(Long id) {
        // Pega o token armazenado no filtro
        String bearerToken = (String) request.getAttribute(Constants.BEARER_TOKEN_ATTRIBUTE);

        webClient.put()
                .uri("/reservations/{id}/sold", id)
                .headers(headers -> headers.setBearerAuth(bearerToken))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

}