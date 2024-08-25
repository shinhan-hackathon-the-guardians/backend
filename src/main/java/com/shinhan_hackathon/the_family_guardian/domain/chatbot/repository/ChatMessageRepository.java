package com.shinhan_hackathon.the_family_guardian.domain.chatbot.repository;

import com.shinhan_hackathon.the_family_guardian.domain.chatbot.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByUserIdOrderByTimestampAsc(Long userId);
}
