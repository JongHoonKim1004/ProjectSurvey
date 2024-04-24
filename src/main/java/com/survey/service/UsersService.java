package com.survey.service;

import com.survey.dto.UsersDTO;
import com.survey.entity.Users;
import com.survey.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UsersService {
    // 이용자가 주인 업무는 여기서 해결
    // 로그인 관련은 보안 들어가면 해결
    @Autowired
    private UsersRepository usersRepository;


    // 이용자 정보만 등록
    @Transactional
    public Users registerUser(UsersDTO dto, String password){
        Users user = Users.builder().
                name(dto.getName()).
                nickname(dto.getNickname()).
                password(password).
                phone(dto.getPhone()).
                addr(dto.getAddr()).
                zipNo(dto.getZipNo()).
                birth(dto.getBirth()).
                gender(dto.getGender()).
                occupation(dto.getOccupation()).
                married(dto.getMarried()).
                build();

        usersRepository.save(user);
        log.info(user.toString());
        log.info("/////////////// REGISTER COMPLETE");
        return user;
    }

    // 이용자 정보 변경 (마이페이지)
    @Transactional
    public UsersRepository modifyUser(UsersDTO dto, String password){
        Users user = usersRepository.findByUsersId(dto.getUsersId());
        user.setPassword(password);
        user.setPhone(dto.getPhone());
        user.setAddr(dto.getAddr());
        user.setZipNo(dto.getZipNo());
        user.setBirth(dto.getBirth());
        user.setGender(dto.getGender());
        user.setOccupation(dto.getOccupation());
        user.setMarried(dto.getMarried());
        usersRepository.save(user);
        log.info(user.toString());
        log.info("/////////////// MODIFY COMPLETE");

        return usersRepository;
    }

    // 이용자 탈퇴
    @Transactional
    public void deleteUser(String UsersId){
        Users user = usersRepository.findByUsersId(UsersId);
        usersRepository.delete(user);
        log.info("DElETE COMPLETE");
    }

    // GET
    // 본인 정보 불러오기
    public UsersRepository getUsersRepository(String UserId) {
        Users user = usersRepository.findByUsersId(UserId);

        return usersRepository;
    }

    // 이용자 목록 불러오기
    public List<UsersDTO> getAllUsers(){
        List<Users> users = usersRepository.findAll();
        List<UsersDTO> usersList = new ArrayList<>();
        for(Users user : users){
            UsersDTO userDTO = new UsersDTO();
            userDTO.setUsersId(user.getUsersId());
            userDTO.setName(user.getName());
            userDTO.setNickname(user.getNickname());
            userDTO.setPhone(user.getPhone());
            userDTO.setAddr(user.getAddr());
            userDTO.setZipNo(user.getZipNo());
            userDTO.setBirth(user.getBirth());
            userDTO.setGender(user.getGender());
            userDTO.setOccupation(user.getOccupation());
            userDTO.setMarried(user.getMarried());
            usersList.add(userDTO);
        }

        return usersList;
    }
}
