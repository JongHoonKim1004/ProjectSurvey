package com.survey.service;

import com.survey.dto.UsersDTO;
import com.survey.dto.UsersPointDTO;
import com.survey.entity.UsersPoint;
import com.survey.repository.UsersPointRepository;
import com.survey.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UsersPointService {
    @Autowired
    private UsersPointRepository usersPointRepository;
    @Autowired
    private UsersRepository usersRepository;

    // Convert DTO to Entity
    public UsersPoint convertDTO(UsersPointDTO usersPointDTO) {
        UsersPoint usersPoint = new UsersPoint();
        if(usersPointDTO.getPointId() != null){
            usersPoint.setPointId(usersPointDTO.getPointId());
        }
        usersPoint.setUsersId(usersRepository.findByUsersId(usersPointDTO.getUsersId()));
        usersPoint.setPointTotal(usersPointDTO.getPointTotal());
        usersPoint.setPointUsed(usersPointDTO.getPointUsed());
        usersPoint.setPointBalance(usersPointDTO.getPointBalance());
        return usersPoint;
    }

    // Convert Entity to DTO
    public UsersPointDTO convertUser(UsersPoint usersPoint) {
        UsersPointDTO usersPointDTO = new UsersPointDTO();
        usersPointDTO.setPointId(usersPoint.getPointId());
        usersPointDTO.setUsersId(usersPoint.getUsersId().getUsersId());
        usersPointDTO.setPointTotal(usersPoint.getPointTotal());
        usersPointDTO.setPointUsed(usersPoint.getPointUsed());
        usersPointDTO.setPointBalance(usersPoint.getPointBalance());

        return usersPointDTO;
    }

    // Create
    // new Customer register & give Point
    @Transactional
    public void save(UsersDTO usersDTO) {
        UsersPoint usersPoint = new UsersPoint();
        usersPoint.setUsersId(usersRepository.findByUsersId(usersDTO.getUsersId()));
        usersPoint.setPointTotal(500);
        usersPoint.setPointUsed(0);
        usersPoint.setPointBalance(usersPoint.getPointTotal() - usersPoint.getPointUsed());
        log.info("User{}'s Point Balance is : {}", usersPoint.getUsersId(), usersPoint.getPointBalance());
        usersPointRepository.save(usersPoint);
        log.info("Saved usersPoint: {}", usersPoint);
    }

    // Read
    // For customer
    public UsersPointDTO findByUsersId(String usersId) {
        UsersPointDTO usersPointDTO = new UsersPointDTO();
        UsersPoint usersPoint = usersPointRepository.findByUsersId(usersRepository.findByUsersId(usersId));
        usersPointDTO = convertUser(usersPoint);
        return usersPointDTO;
    }

    // For Admin
    public List<UsersPointDTO> findAll() {
        List<UsersPoint> usersPoints = usersPointRepository.findAll();
        List<UsersPointDTO> usersPointDTOs = new ArrayList<>();
        for (UsersPoint usersPoint : usersPoints) {
            usersPointDTOs.add(convertUser(usersPoint));
        }
        return usersPointDTOs;
    }

    // Update
    @Transactional
    public void update(UsersPointDTO usersPointDTO) {
        UsersPoint usersPoint = convertDTO(usersPointDTO);
        usersPointRepository.save(usersPoint);
        log.info("Updated usersId: {}", usersPoint.getUsersId().getUsersId());
    }


    // Delete
    public void delete(String pointId) {
        UsersPoint usersPoint = usersPointRepository.findByPointId(pointId);
        usersPointRepository.delete(usersPoint);
        log.info("Deleted usersId: {}", usersPoint.getUsersId().getUsersId());
    }

    // Give Point
    @Transactional
    public UsersPointDTO givePoint(UsersPointDTO usersPointDTO, Integer point) {
        UsersPointDTO newUsersPointDTO = usersPointDTO;
        newUsersPointDTO.setPointTotal(usersPointDTO.getPointTotal() + point);
        newUsersPointDTO.setPointBalance(usersPointDTO.getPointBalance() + point);

        UsersPoint usersPoint = convertDTO(newUsersPointDTO);
        usersPointRepository.save(usersPoint);

        log.info("Giving point:{}, Balance : {}", point, newUsersPointDTO.getPointBalance());

        return newUsersPointDTO;
    }

    // Use Point
    @Transactional
    public UsersPointDTO usePoint(UsersPointDTO usersPointDTO, Integer point) {
        UsersPointDTO newUsersPointDTO = usersPointDTO;
        newUsersPointDTO.setPointUsed(usersPointDTO.getPointUsed() + point);
        newUsersPointDTO.setPointBalance(usersPointDTO.getPointBalance() - point);
        log.info("Using point:{}, Balance : {}", point, newUsersPointDTO.getPointBalance());

        return usersPointDTO;
    }
}

