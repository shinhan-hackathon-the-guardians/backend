package com.shinhan_hackathon.the_family_guardian.domain.chatbot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "ChatbotPrompt")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ChatbotPrompt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String prompt;

    @Builder
    public ChatbotPrompt(String prompt) {
        this.prompt = prompt;
    }
}
