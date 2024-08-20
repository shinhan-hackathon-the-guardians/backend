package com.shinhan_hackathon.the_family_guardian.domain.family.entity;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Family")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50 ,nullable = false)
    private String name;

    @Column(length = 100)
    private String description;

    @Column(nullable = false)
    private int approvalRequirement = 1; // 승인 요구 가디언 수

    @OneToMany(mappedBy = "family")
    private List<User> users;

    @Builder
    public Family(String name, String description, int approvalRequirement, List<User> users) {
        this.name = name;
        this.description = description;
        this.approvalRequirement = approvalRequirement;
        this.users = users;
    }
}
