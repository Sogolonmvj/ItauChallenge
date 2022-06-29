package com.vieira.sogolon.ItauChallenge.repository;

import com.vieira.sogolon.ItauChallenge.entities.UserCritic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<UserCritic, Long> {
//    Optional<UserCritic> findByToken(UUID token);

    Optional<UserCritic> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserCritic critic " +
            "SET critic.enabled = TRUE WHERE critic.email = ?1")
    int enableUser(String email);
}
