package com.survey.repository;

import com.survey.entity.Users;
import com.survey.entity.UsersPointLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersPointLogRepository extends JpaRepository<UsersPointLog, String> {
    public List<UsersPointLog> findByUsersIdOrderByLogIdDesc(Users UsersId);

    public UsersPointLog findByLogId(Long logId);

    public List<UsersPointLog> findAllByOrderByLogIdDesc();
}
