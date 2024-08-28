package com.shinhan_hackathon.the_family_guardian.domain.notification.entity;

import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.Transaction;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Transaction 주인, 조회 용이성을 위해 사용
    private User user;

    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private PaymentLimitType limitType; // 알림 종류 : 단건 제한, 사용 한도

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean requiresResponse; // 응답이 필요한 알림인지

    @Builder
    public Notification(User user, Transaction transaction, PaymentLimitType limitType, String title, String body,
                        boolean requiresResponse) {
        this.user = user;
        this.transaction = transaction;
        this.limitType = limitType;
        this.title = title;
        this.body = body;
        this.requiresResponse = requiresResponse;
    }
}
