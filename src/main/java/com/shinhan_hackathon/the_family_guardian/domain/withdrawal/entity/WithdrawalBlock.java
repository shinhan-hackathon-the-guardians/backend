package com.shinhan_hackathon.the_family_guardian.domain.withdrawal.entity;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "WithdrawalBlock")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class WithdrawalBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int limitAmount;

    @Builder
    public WithdrawalBlock(User user, int limitAmount) {
        this.user = user;
        this.limitAmount = limitAmount;
    }
}
