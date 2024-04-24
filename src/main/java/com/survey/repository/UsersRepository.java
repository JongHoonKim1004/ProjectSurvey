package com.survey.repository;

import com.survey.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, String> {
    public Users findByUsersId(String usersId);
}
