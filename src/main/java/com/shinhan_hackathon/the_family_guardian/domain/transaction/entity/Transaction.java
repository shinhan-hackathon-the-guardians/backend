package com.shinhan_hackathon.the_family_guardian.domain.transaction.entity;

import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransactionHistoryListResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransactionHistoryResponse;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import jakarta.persistence.*;

import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Transaction")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Transaction {

    // TODO: 제공되는 API에 맞게 수정할 필요 있음
    // TODO: 현재 Notification의 승인 요청 결과에 대해 승인된 개수 보관 필요, approvedCount 필드 사용?

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private Timestamp timestamp;

    @Column(nullable = false, length = 20)
    private String status;

    private int approveCount; // 승인된 요청 횟수

    @Builder
    public Transaction(User user, TransactionType transactionType, Long amount, Timestamp timestamp, String status) {
        this.user = user;
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = timestamp;
        this.status = status;
        this.approveCount = 0;
    }

    public static Transaction toTransaction(AccountTransactionHistoryListResponse.Rec.Transaction transaction) {
        return Transaction.builder()

            .build();
    }
}
