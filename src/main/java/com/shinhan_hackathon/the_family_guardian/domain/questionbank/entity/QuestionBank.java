package com.shinhan_hackathon.the_family_guardian.domain.questionbank.entity;

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
@Table(name = "QuestionBank")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class QuestionBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false, length = 10)
    private String answer;

    @Column(nullable = false)
    private String explanation;

    @Builder
    public QuestionBank(String question, String answer, String explanation) {
        this.question = question;
        this.answer = answer;
        this.explanation = explanation;
    }
}
