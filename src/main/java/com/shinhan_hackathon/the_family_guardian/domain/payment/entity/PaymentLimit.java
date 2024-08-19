package com.shinhan_hackathon.the_family_guardian.domain.payment.entity;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "PaymentLimit")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PaymentLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Timestamp startDate;

    @Column(length = 20)
    private String period;

    private Integer singleTransactionLimit;
    private Integer maxLimitAmount;
    private Integer amountUsed;

    @Builder
    public PaymentLimit(User user, Timestamp startDate, String period, Integer singleTransactionLimit,
                        Integer maxLimitAmount, Integer amountUsed) {
        this.user = user;
        this.startDate = startDate;
        this.period = period;
        this.singleTransactionLimit = singleTransactionLimit;
        this.maxLimitAmount = maxLimitAmount;
        this.amountUsed = amountUsed;
    }
}
