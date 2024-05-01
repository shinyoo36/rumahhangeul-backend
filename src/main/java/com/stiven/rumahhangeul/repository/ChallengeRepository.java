package com.stiven.rumahhangeul.repository;

import com.stiven.rumahhangeul.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    List<Challenge> findAllByUserId(Long userId);

    Optional<Challenge> findByNamaChallengeAndUserId(String namaChallenge, Long userId);

}
