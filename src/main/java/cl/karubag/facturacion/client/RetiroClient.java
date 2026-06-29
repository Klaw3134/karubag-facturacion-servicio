package cl.karubag.facturacion.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class RetiroClient {

    private final WebClient webClient;

    public RetiroClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://karubag-retiro-servicio.onrender.com")
                .build();
    }

    public boolean existeRetiro(Long retiroId) {
        try {
            webClient.get()
                    .uri("/api/retiros/" + retiroId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
