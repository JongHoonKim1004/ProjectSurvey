package com.survey.controller;

import com.survey.dto.ErrorDTO;
import com.survey.dto.UsersDTO;
import com.survey.dto.UsersPointDTO;
import com.survey.dto.UsersPointLogDTO;
import com.survey.entity.Users;
import com.survey.security.TokenProvider;
import com.survey.service.UsersPointLogService;
import com.survey.service.UsersPointService;
import com.survey.service.UsersService;
import com.survey.service.UsersSurveyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users/*")
@Slf4j
public class UsersController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private UsersSurveyService usersSurveyService;
    @Autowired
    private UsersPointService usersPointService;
    @Autowired
    private UsersPointLogService usersPointLogService;

    @Autowired
    private TokenProvider tokenProvider;


    // 개인정보
        // Create
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UsersDTO usersDTO) {
        // 1. 개인정보 생성
        UsersDTO usersDTO1 = usersService.save(usersDTO);
        log.info("User created: {}", usersDTO1);

        // 2. 이용자 포인트 테이블 생성
        usersPointService.save(usersDTO1); // 사업자와 달리 이미 로직을 만들어 둠

        // 3. 이용자 첫 포인트 이력 생성
        UsersPointLogDTO usersPointLogDTO = new UsersPointLogDTO();
        usersPointLogDTO.setUsersId(usersDTO1.getUsersId());
        usersPointLogDTO.setPointChange(500);
        usersPointLogDTO.setChangeType("신규 가입");
        usersPointLogDTO.setChangeDate(LocalDateTime.now());
        usersPointLogService.save(usersPointLogDTO);
        log.info("User {}'s Point Log Created", usersDTO1.getUsersId());

        return ResponseEntity.ok("User created");

    }

        // Read
            // Get Myself
    @GetMapping("/read/{usersId}")
    public ResponseEntity<UsersDTO> readUser(@PathVariable("usersId") String usersId) {
        UsersDTO usersDTO = usersService.getMyself(usersId);
        log.info("User read: {}", usersDTO);
        return ResponseEntity.ok(usersDTO);
    }

            // Get All For Admin
    @GetMapping("/list")
    public ResponseEntity<List<UsersDTO>> listUsers() {
        List<UsersDTO> usersDTOList = usersService.getAllUsers();
        return ResponseEntity.ok(usersDTOList);
    }

        // Update
    @PostMapping("/update/{usersId}")
    public ResponseEntity<String> updateUser(@PathVariable("usersId") String usersId, @RequestBody UsersDTO usersDTO) {
        usersDTO.setUsersId(usersId);
        usersService.save(usersDTO);
        log.info("User updated: {}", usersDTO);
        return ResponseEntity.ok("User updated");
    }

        // Delete
    @PostMapping("/delete/{usersId}")
    public ResponseEntity<String> deleteUser(@PathVariable("usersId") String usersId) {
        // 1. 회원 포인트 이력 전부 제거
        List<UsersPointLogDTO> usersPointLogDTOList = usersPointLogService.findByUsersId(usersId);
        for(UsersPointLogDTO usersPointLogDTO : usersPointLogDTOList) {
            usersPointLogService.delete(usersPointLogDTO.getLogId());
            log.info("Users Point Log Deleted: {}", usersPointLogDTO.getLogId());
        }
        log.info("User {}'s Point Log Deleted", usersId);

        // 2. 회원 포인트 테이블 제거
        UsersPointDTO usersPointDTO = usersPointService.findByUsersId(usersId);
        usersPointService.delete(usersPointDTO.getPointId());
        log.info("User {}'s Point Deleted", usersId);

        // 3. 회원 정보 제거
        usersService.delete(usersId);
        log.info("User {} deleted", usersId);

        return ResponseEntity.ok("User deleted");
    }

    // 이용자 포인트 관련 메서드
        // Read
            // Get Myself
    @GetMapping("/point/find/{usersId}")
    public ResponseEntity<UsersPointDTO> findPoint(@PathVariable("usersId") String usersId) {
        UsersPointDTO usersPointDTO = usersPointService.findByUsersId(usersId);
        return ResponseEntity.ok(usersPointDTO);
    }

            // Get List For Admin
    @GetMapping("/point/list")
    public ResponseEntity<List<UsersPointDTO>> listPoints() {
        List<UsersPointDTO> usersPointDTOList = usersPointService.findAll();
        return ResponseEntity.ok(usersPointDTOList);
    }

        // Update
            // 포인트 적립은 조사 완료와 함께 진행
            // Use Point
    @PostMapping("/point/use/{usersId}")
    public ResponseEntity<String> usePoint(@PathVariable("usersId") String usersId, @RequestParam("point") Integer point) {
        // 포인트 테이블 처리
        UsersPointDTO usersPointDTO = usersPointService.findByUsersId(usersId);
        Integer pointBalance = usersPointDTO.getPointBalance();
        Integer pointUsed = usersPointDTO.getPointUsed();
            // 포인트가 모자라면 오류 코드 전송
        if(pointBalance - pointUsed < 0) {
            return ResponseEntity.badRequest().body("Not Enough Point!");
        }
            // 포인트가 남는다면 그대로 진행
        usersPointDTO.setPointUsed(pointUsed + point);
        usersPointDTO.setPointBalance(pointBalance - point);
        usersPointService.update(usersPointDTO);
        log.info("User {}'s Point Used : {}", usersId, point);

        // 포인트 이력 처리
        UsersPointLogDTO usersPointLogDTO = new UsersPointLogDTO();
        usersPointLogDTO.setUsersId(usersId);
        usersPointLogDTO.setPointChange(point * -1);
        usersPointLogDTO.setChangeType("포인트 사용");
        usersPointLogDTO.setChangeDate(LocalDateTime.now());
        usersPointLogService.save(usersPointLogDTO);
        log.info("User {]'s Point Log Created", usersId);

        return ResponseEntity.ok("User Point Updated");
    }

    // 이용자 포인트 이력 전용 메서드
        // Read
            // Get List For Myself
    @GetMapping("/pointlog/list/{usersId}")
    public ResponseEntity<List<UsersPointLogDTO>> getListForMyself(@PathVariable String usersId){
        List<UsersPointLogDTO> usersPointLogDTOList = usersPointLogService.findByUsersId(usersId);
        return ResponseEntity.ok(usersPointLogDTOList);
    }

            // Get List For Admin
    @GetMapping("/pointlog/list/all")
    public ResponseEntity<List<UsersPointLogDTO>> getListAll(){
        List<UsersPointLogDTO> usersPointLogDTOList = usersPointLogService.findAll();
        return ResponseEntity.ok(usersPointLogDTOList);
    }

        // Delete (이력 개별 삭제, 통합 삭제는 회원탈퇴를 이용해서만 이루어짐)
    @PostMapping("/pointlog/delete/{logId}")
    public ResponseEntity<String> deletePointLog(@PathVariable("logId") Long logId) {
        usersPointLogService.delete(logId);
        log.info("User's log Deleted: {}", logId);
        return ResponseEntity.ok("Log Deleted");
    }

    /*                              로그인                                         */
    // 로그인 처리

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @PostMapping("/login")
    public ResponseEntity<?> usersLogin(@RequestBody UsersDTO usersDTO){
        Users users = usersService.getByCredentials(usersDTO.getName(), usersDTO.getPassword(), passwordEncoder);
        log.info("User found : {}", users);
        if(users != null){
            final String token = tokenProvider.create(users);
            final UsersDTO usersDTO1 = UsersDTO.builder()
                    .name(users.getName())
                    .nickname(users.getNickname())
                    .token(token)
                    .build();

            return ResponseEntity.ok(usersDTO1);
        } else {
            ErrorDTO errorDTO = new ErrorDTO().builder().error("Invalid username or password").build();
            return ResponseEntity.badRequest().body(errorDTO);
        }
    }
}
