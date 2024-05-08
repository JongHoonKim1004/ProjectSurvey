package com.survey.repository;

import com.survey.entity.Member;
import com.survey.entity.MemberPointLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberPointLogRepository extends JpaRepository<MemberPointLog, String> {
    public List<MemberPointLog> findByMemberIdOrderByChangeDateDesc(Member byMemberId);

    public MemberPointLog findByLogId(long logId);

    public List<MemberPointLog> findAllByOrderByChangeDateDesc();
}
