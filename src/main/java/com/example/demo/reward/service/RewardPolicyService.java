package com.example.demo.reward.service;

import com.example.demo.reward.dto.RewardPolicyRequest;
import com.example.demo.reward.dto.RewardPolicyResponse;
import java.util.List;
import java.util.Optional;

public interface RewardPolicyService {
  List<RewardPolicyResponse> findAll();

  Optional<RewardPolicyResponse> findById(Long id);

  Optional<RewardPolicyResponse> create(RewardPolicyRequest request);

  Optional<RewardPolicyResponse> update(Long id, RewardPolicyRequest request);

  boolean delete(Long id);
}
