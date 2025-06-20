package com.diegotrigo.chatbot.chatbot_backend.repository;

import com.diegotrigo.chatbot.chatbot_backend.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByUserIdOrderByTimestampAsc(String userId);
}
