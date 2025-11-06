package com.example.demo.reward.repository;

import com.example.demo.reward.domain.RewardPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardPolicyRepository extends JpaRepository<RewardPolicy, Long> {
}
