package com.example.demo.reward.repository;

import com.example.demo.reward.domain.RewardIssue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardIssueRepository extends JpaRepository<RewardIssue, Long> {
}
