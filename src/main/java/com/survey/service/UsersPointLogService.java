package com.survey.service;

import com.survey.dto.UsersPointLogDTO;
import com.survey.entity.UsersPointLog;
import com.survey.repository.UsersPointLogRepository;
import com.survey.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UsersPointLogService {
    @Autowired
    private UsersPointLogRepository usersPointLogRepository;
    @Autowired
    private UsersRepository usersRepository;


    // Convert DTO to Entity
    public UsersPointLog convertDTO(UsersPointLogDTO usersPointLogDTO) {
        UsersPointLog usersPointLog = new UsersPointLog();
        if(usersPointLogDTO.getLogId() != null) {
            usersPointLog.setLogId(usersPointLogDTO.getLogId());
        }
        usersPointLog.setUsersId(usersRepository.findByUsersId(usersPointLogDTO.getUsersId()));
        usersPointLog.setPointChange(usersPointLogDTO.getPointChange());
        usersPointLog.setChangeType(usersPointLogDTO.getChangeType());
        if(usersPointLogDTO.getChangeDate() != null){
            usersPointLog.setChangeDate(usersPointLogDTO.getChangeDate());
        } else {
            usersPointLog.setChangeDate(LocalDateTime.now());
        }

        return usersPointLog;
    }

    // Convert Entity to DTO
    public UsersPointLogDTO convertUser(UsersPointLog usersPointLog) {
        UsersPointLogDTO usersPointLogDTO = new UsersPointLogDTO();
        usersPointLogDTO.setLogId(usersPointLog.getLogId());
        usersPointLogDTO.setUsersId(usersPointLog.getUsersId().getUsersId());
        usersPointLogDTO.setPointChange(usersPointLog.getPointChange());
        usersPointLogDTO.setChangeType(usersPointLog.getChangeType());
        usersPointLogDTO.setChangeDate(usersPointLog.getChangeDate());

        return usersPointLogDTO;
    }

    // Create
        // Normal
    @Transactional
    public UsersPointLogDTO save(UsersPointLogDTO usersPointLogDTO) {
        UsersPointLog usersPointLog = convertDTO(usersPointLogDTO);
        UsersPointLog usersPointLog1 = usersPointLogRepository.save(usersPointLog);
        log.info("User {}s point log saved", usersPointLogDTO.getUsersId());

        return convertUser(usersPointLog1);
    }

        // Create Plus log
    @Transactional
    public UsersPointLogDTO savePlusLog(String userId, Integer point, String changeType) {
        UsersPointLogDTO usersPointLogDTO = new UsersPointLogDTO();
        usersPointLogDTO.setUsersId(userId);
        usersPointLogDTO.setPointChange(point);
        usersPointLogDTO.setChangeType(changeType);

        usersPointLogRepository.save(convertDTO(usersPointLogDTO));

        return usersPointLogDTO;
    }

        // Create Minus log
    @Transactional
    public UsersPointLogDTO saveMinusLog(String userId, Integer point, String changeType) {
        UsersPointLogDTO usersPointLogDTO = new UsersPointLogDTO();
        usersPointLogDTO.setUsersId(userId);
        usersPointLogDTO.setPointChange(point * -1);
        usersPointLogDTO.setChangeType(changeType);

        usersPointLogRepository.save(convertDTO(usersPointLogDTO));

        return usersPointLogDTO;
    }

    // Read
    // For customer
    public List<UsersPointLogDTO> findByUsersId(String usersId) {
        List<UsersPointLog> usersPointLog = usersPointLogRepository.findByUsersIdOrderByLogIdDesc(usersRepository.findByUsersId(usersId));
        List<UsersPointLogDTO> usersPointLogDTOList = new ArrayList<>();
        for (UsersPointLog usersPointLog1 : usersPointLog) {
            UsersPointLogDTO usersPointLogDTO = convertUser(usersPointLog1);
            usersPointLogDTOList.add(usersPointLogDTO);
        }

        return usersPointLogDTOList;
    }

    // For Admin
    public List<UsersPointLogDTO> findAll(){
        List<UsersPointLog> usersPointLog = usersPointLogRepository.findAllByOrderByLogIdDesc();
        List<UsersPointLogDTO> usersPointLogDTOList = new ArrayList<>();
        for (UsersPointLog usersPointLog1 : usersPointLog) {
            UsersPointLogDTO usersPointLogDTO = convertUser(usersPointLog1);
            usersPointLogDTOList.add(usersPointLogDTO);
        }

        return usersPointLogDTOList;
    }

    // Update
    @Transactional
    public void update(UsersPointLogDTO usersPointLogDTO) {
        UsersPointLog usersPointLog = convertDTO(usersPointLogDTO);
        usersPointLogRepository.save(usersPointLog);
        log.info("User {}s point log updated", usersPointLogDTO.getUsersId());
    }

    // Delete
    @Transactional
    public void delete(Long logId) {
        UsersPointLog usersPointLog = usersPointLogRepository.findByLogId(logId);
        usersPointLogRepository.delete(usersPointLog);
        log.info("User {}s point log deleted", usersPointLog.getUsersId());
    }


}
