package com.survey.service;

import com.survey.dto.AdminDTO;
import com.survey.entity.Admin;
import com.survey.entity.Users;
import com.survey.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public AdminDTO save(AdminDTO adminDTO) {
        Admin admin = convertDTO(adminDTO);
        Admin savedAdmin = adminRepository.save(admin);
        log.info("Admin saved: {}", savedAdmin.getAdminId());

        return convertEntity(savedAdmin);
    }

    // Read
    // Get List
    public List<AdminDTO> getAllAdmins() {
        List<Admin> admins = adminRepository.findAll();
        List<AdminDTO> adminDTOs = new ArrayList<>();
        for(Admin admin : admins){
            adminDTOs.add(convertEntity(admin));
        }
        return adminDTOs;
    }

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

    // Find By name
    public AdminDTO findByName(String name){
        Admin admin = adminRepository.findByName(name);
        return admin != null ? convertEntity(admin) : null ;
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

    // User check
    public Admin getByCredentials(final String name, final String password, final PasswordEncoder passwordEncoder) {
        final Admin originalUser = adminRepository.findByName(name);

        if(originalUser != null && passwordEncoder.matches(password, originalUser.getPassword())){
            return originalUser;
        }

        return null;
    }
}
