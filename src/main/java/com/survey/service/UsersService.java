package com.survey.service;

import com.survey.dto.UsersDTO;
import com.survey.entity.Users;
import com.survey.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Convert DTO to Entity
    public Users convertDTO(UsersDTO usersDTO) {
        Users users = new Users();
        if(usersDTO.getUsersId() != null){
            users.setUsersId(usersDTO.getUsersId());
        }
        users.setName(usersDTO.getName());
        users.setPassword(usersDTO.getPassword());
        users.setNickname(usersDTO.getNickname());
        users.setPhone(usersDTO.getPhone());
        users.setAddr(usersDTO.getAddr());
        users.setAddrDetail(usersDTO.getAddrDetail());
        users.setZipNo(usersDTO.getZipNo());
        users.setBirth(usersDTO.getBirth());
        users.setGender(usersDTO.getGender());
        users.setOccupation(usersDTO.getOccupation());
        users.setMarried(usersDTO.getMarried());

        return users;
    }

    // Convert Entity to DTO
    public UsersDTO convertEntity(Users users) {
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setUsersId(users.getUsersId());
        usersDTO.setName(users.getName());
        usersDTO.setPassword(users.getPassword());
        usersDTO.setNickname(users.getNickname());
        usersDTO.setPhone(users.getPhone());
        usersDTO.setAddr(users.getAddr());
        usersDTO.setAddrDetail(users.getAddrDetail());
        usersDTO.setZipNo(users.getZipNo());
        usersDTO.setBirth(users.getBirth());
        usersDTO.setGender(users.getGender());
        usersDTO.setOccupation(users.getOccupation());
        usersDTO.setMarried(users.getMarried());

        return usersDTO;
    }

    // Create
    @Transactional
    public UsersDTO save(UsersDTO usersDTO){
        Users users = convertDTO(usersDTO);
        String encodedPassword = passwordEncoder.encode(users.getPassword());
        users.setPassword(encodedPassword);
        Users savedUsers = usersRepository.save(users);
        log.info("User Saved: {}", savedUsers);

        return convertEntity(savedUsers);
    }

    // Read
    // Get List
    public List<UsersDTO> getAllUsers(){
        List<UsersDTO> usersDTOList = new ArrayList<>();
        List<Users> users = usersRepository.findAll();
        for(Users user : users){
            UsersDTO usersDTO = convertEntity(user);
            usersDTOList.add(usersDTO);
        }
        return usersDTOList;
    }

    // Get Myself
    public UsersDTO getMyself(String usersId){
        Users users = usersRepository.findByUsersId(usersId);
        UsersDTO usersDTO = convertEntity(users);
        return usersDTO;
    }

    // Find By name
    public UsersDTO findByName(String name){
        Users users = usersRepository.findByName(name);
        return users != null ? convertEntity(users) : null ;
    }


    // Update
    @Transactional
    public void update(UsersDTO usersDTO){
        Users users = convertDTO(usersDTO);
        String encodedPassword = passwordEncoder.encode(users.getPassword());
        users.setPassword(encodedPassword);
        Users savedUsers = usersRepository.save(users);
        log.info("User Updated: {}", savedUsers);
    }

    // Delete
    @Transactional
    public void delete(String usersId){
        Users users = usersRepository.findByUsersId(usersId);
        usersRepository.delete(users);
        log.info("User Deleted: {}", users);

    }

    // 로그인 관련
    // User check
    public Users getByCredentials(final String name, final String password, final PasswordEncoder passwordEncoder) {
        final Users originalUser = usersRepository.findByName(name);

        if(originalUser != null && passwordEncoder.matches(password, originalUser.getPassword())){
            return originalUser;
        }

        return null;
    }
}
