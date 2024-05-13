package com.survey.repository;

import com.survey.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
    public Member findByMemberId(String memberId);

    public Member findByName(String name);
}
