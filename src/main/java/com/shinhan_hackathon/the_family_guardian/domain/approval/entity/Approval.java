package com.shinhan_hackathon.the_family_guardian.domain.approval.entity;

import com.shinhan_hackathon.the_family_guardian.domain.family.entity.Family;
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
@Table(name = "Approval")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 20)
    private String relationship;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean accepted;

    @Builder
    public Approval(Family family, User user, String relationship, boolean accepted) {
        this.family = family;
        this.user = user;
        this.relationship = relationship;
        this.accepted = accepted;
    }
}
