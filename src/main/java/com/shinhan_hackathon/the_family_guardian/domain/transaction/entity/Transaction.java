package com.shinhan_hackathon.the_family_guardian.domain.transaction.entity;

import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransactionHistoryListResponse;
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
    private Long transactionBalance;

    @Column(nullable = false)
    private Timestamp timestamp; // 요청을 받은 Timestamp, Timeout 확인을 위해 사용

    @Column(nullable = false, length = 20)
    private TransactionStatus status; // 요청의 최종 결과 -> 차단, 승인

    private int approveCount; // 승인된 요청 횟수
    private int rejectCount; // 거절된 요청 횟수

    private String receiver;

    public int incrementApproveCount() {
        return ++this.approveCount;
    }
    public int incrementRejectCount() {
        return ++this.rejectCount;
    }

    public TransactionStatus updateTransactionStatus(TransactionStatus transactionStatus) {
        return this.status = transactionStatus;
    }

    @Builder
    public Transaction(User user, TransactionType transactionType, Long transactionBalance, Timestamp timestamp, TransactionStatus status, int approveCount, int rejectCount, String receiver) {
        this.user = user;
        this.transactionType = transactionType;
        this.transactionBalance = transactionBalance;
        this.timestamp = timestamp;
        this.status = status;
        this.approveCount = approveCount;
        this.rejectCount = rejectCount;
        this.receiver = receiver;
    }

    // TODO: Transaction의 결과를 FCM으로 통지할 때 사용할 수도 있고, 아니면 그냥 임의의 값으로 전송해도 괜찮을 듯
    public static Transaction toTransaction(AccountTransactionHistoryListResponse.Rec.Transaction transaction) {
        return Transaction.builder()

            .build();
    }

}
