package com.example.demo.reward.repository;

import com.example.demo.reward.domain.RewardIssue;
import com.example.demo.reward.dto.RewardIssueExcelRow;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RewardIssueRepository extends JpaRepository<RewardIssue, Long> {

  long countByEvent_IdAndUser_Id(Long eventId, Long userId);

  boolean existsByRewardPolicy_IdAndRewardDate(Long policyId, LocalDate rewardDate);

  long countByRewardPolicy_IdAndUser_Id(Long policyId, Long userId);

  long countByRewardPolicy_IdAndUser_IdAndRewardDate(Long policyId, Long userId, LocalDate rewardDate);

  @Query("""
      select ri from RewardIssue ri
      where (:eventId is null or ri.event.id = :eventId)
        and ri.issuedDt between :startDt and :endDt
      """)
  Page<RewardIssue> findIssues(
      @Param("eventId") Long eventId,
      @Param("startDt") Instant startDt,
      @Param("endDt") Instant endDt,
      Pageable pageable);

  @Query("""
      select new com.example.demo.reward.dto.RewardIssueExcelRow(
          ri.issuedDt,
          ri.rewardDate,
          u.name,
          u.employeeNumber,
          rp.name)
      from RewardIssue ri
      join ri.event e
      join ri.user u
      join ri.rewardPolicy rp
      where (:eventId is null or e.id = :eventId)
        and ri.issuedDt between :startDt and :endDt
      order by ri.issuedDt asc
      """)
  List<RewardIssueExcelRow> findExcelRows(
      @Param("eventId") Long eventId,
      @Param("startDt") Instant startDt,
      @Param("endDt") Instant endDt);
}
