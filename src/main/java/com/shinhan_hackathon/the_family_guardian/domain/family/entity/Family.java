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

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private int approvalRequirement;

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;

    @Builder
    public Family(String name, String description, int approvalRequirement, List<User> users) {
        this.name = name;
        this.description = description;
        this.approvalRequirement = approvalRequirement;
        this.users = users;
    }
}
