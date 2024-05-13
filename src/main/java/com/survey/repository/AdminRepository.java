package com.survey.repository;

import com.survey.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {
    public Admin findByEmployeeNo(String employeeNo);
    public Admin findByName(String name);

    public Admin findByAdminId(String adminId);

    public Admin findByNickname(String writer);
}
