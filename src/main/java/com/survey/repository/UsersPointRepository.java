package com.survey.repository;

import com.survey.entity.Users;
import com.survey.entity.UsersPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersPointRepository extends JpaRepository<UsersPoint, String> {
    public UsersPoint findByUsersId(Users byUsersId);

    public UsersPoint findByPointId(String pointId);
}
