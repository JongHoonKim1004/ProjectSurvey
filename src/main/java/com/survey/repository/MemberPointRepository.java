package com.survey.repository;

import com.survey.entity.Member;
import com.survey.entity.MemberPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPointRepository extends JpaRepository<MemberPoint, String> {
    public MemberPoint findByMemberId(Member byMemberId);

    public MemberPoint findByPointId(String pointId);
}
