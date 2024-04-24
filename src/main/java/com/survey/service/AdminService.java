package com.survey.service;

import com.survey.dto.AdminDTO;
import com.survey.entity.Admin;
import com.survey.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    // Convert DTO to Entity
    public Admin convertDTO(AdminDTO adminDTO) {
        Admin admin = new Admin();
        if(adminDTO.getAdminId() != null){
            admin.setAdminId(adminDTO.getAdminId());
        }
        admin.setName(adminDTO.getName());
        admin.setPassword(adminDTO.getPassword());
        admin.setNickname(adminDTO.getNickname());
        admin.setPhone(adminDTO.getPhone());
        admin.setEmployeeNo(adminDTO.getEmployeeNo());
        return admin;
    }

    // Convert Entity to DTO
    public AdminDTO convertEntity(Admin admin) {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setAdminId(admin.getAdminId());
        adminDTO.setName(admin.getName());
        adminDTO.setPassword(admin.getPassword());
        adminDTO.setNickname(admin.getNickname());
        adminDTO.setPhone(admin.getPhone());
        adminDTO.setEmployeeNo(admin.getEmployeeNo());
        return adminDTO;
    }

    // Create
    @Transactional
    public void save(AdminDTO adminDTO) {
        Admin admin = convertDTO(adminDTO);
        Admin savedAdmin = adminRepository.save(admin);
        log.info("Admin saved: {}", savedAdmin.getAdminId());
    }

    // Read
    // Get Myself
    public AdminDTO getMyself(String adminId){
        Admin admin = adminRepository.findByAdminId(adminId);
        return convertEntity(admin);
    }

    // Find By EmployNo
    public AdminDTO findByEmployeeNo(String employeeNo){
        Admin admin = adminRepository.findByEmployeeNo(employeeNo);
        return convertEntity(admin);
    }

    // Update
    @Transactional
    public void update(AdminDTO adminDTO) {
        Admin admin = convertDTO(adminDTO);
        Admin savedAdmin = adminRepository.save(admin);
        log.info("Admin updated: {}", savedAdmin.getAdminId());
    }

    // Delete
    @Transactional
    public void delete(String adminId) {
        Admin admin = adminRepository.findByAdminId(adminId);
        adminRepository.delete(admin);
        log.info("Admin Deleted: {}", admin.getAdminId());
    }


}
