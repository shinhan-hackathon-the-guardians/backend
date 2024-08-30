package com.shinhan_hackathon.the_family_guardian.domain.user.repository;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByPhone(String phoneNUmber);

	Optional<User> findByAccountNumber(String accountNumber);
}
