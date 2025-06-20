package com.diegotrigo.chatbot.chatbot_backend.controller;

import com.diegotrigo.chatbot.chatbot_backend.dto.ChatMessageDTO;
import com.diegotrigo.chatbot.chatbot_backend.model.ChatMessage;
import com.diegotrigo.chatbot.chatbot_backend.repository.ChatMessageRepository;
import com.diegotrigo.chatbot.chatbot_backend.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private OpenAIService openAIService;

    @PostMapping("/send")
    public Mono<ChatMessage> receiveMessage(@RequestBody ChatMessageDTO dto) {
        ChatMessage userMessage = new ChatMessage(
                dto.getUserId(),
                dto.getMessage(),
                true,
                LocalDateTime.now()
        );

        chatMessageRepository.save(userMessage);

        return openAIService.getChatCompletion(dto.getMessage())
                .map(responseText -> {
                    ChatMessage botResponse = new ChatMessage(
                            dto.getUserId(),
                            responseText,
                            false,
                            LocalDateTime.now()
                    );
                    chatMessageRepository.save(botResponse);
                    return botResponse;
                })
                .onErrorResume(e -> {
                    String errorMessage = "Error con OpenAI: " + e.getMessage();
                    ChatMessage errorResponse = new ChatMessage(
                            dto.getUserId(),
                            errorMessage,
                            false,
                            LocalDateTime.now()
                    );
                    chatMessageRepository.save(errorResponse);
                    return Mono.just(errorResponse);
                });
    }
}
