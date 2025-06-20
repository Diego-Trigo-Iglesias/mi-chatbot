package com.diegotrigo.chatbot.chatbot_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public OpenAIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1").build();
    }

    public Mono<String> getChatCompletion(String prompt) {
        return webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue("""
                    {
                      "model": "gpt-3.5-turbo",
                      "messages": [
                        {"role": "user", "content": "%s"}
                      ]
                    }
                """.formatted(prompt))
                .retrieve()
                .bodyToMono(String.class);
    }
}
