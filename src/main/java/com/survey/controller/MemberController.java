package com.survey.controller;

import com.survey.dto.MemberDTO;
import com.survey.dto.MemberPointDTO;
import com.survey.dto.MemberPointLogDTO;
import com.survey.entity.MemberPoint;
import com.survey.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/member/*")
@Slf4j
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberPointService memberPointService;
    @Autowired
    private MemberPointLogService memberPointLogService;


    // 개인 정보 관련 메서드
        // Create (계정을 생성 할때 포인트 테이블도 같이 생성)
    @Transactional
    @PostMapping("/create")
    public ResponseEntity<String> createMember(@RequestBody MemberDTO memberDTO) {
        // 개인정보 부분 생성
        MemberDTO memberDTO1 = memberService.save(memberDTO);
        log.info("Member Saved : {}", memberDTO1.toString());

        // 포인트 테이블 생성
        MemberPointDTO memberPointDTO = new MemberPointDTO();
        memberPointDTO.setMemberId(memberDTO1.getMemberId());
        memberPointDTO.setPointTotal(500);
        memberPointDTO.setPointUsed(0);
        memberPointDTO.setPointBalance(memberPointDTO.getPointTotal() - memberPointDTO.getPointUsed());
        MemberPointDTO memberPointDTO1 = memberPointService.save(memberPointDTO);
        log.info("Member Point Created: {}", memberPointDTO1);

        // 포인트 이력 첫 로그 생성
        MemberPointLogDTO memberPointLogDTO = new MemberPointLogDTO();
        memberPointLogDTO.setMemberId(memberDTO1.getMemberId());
        memberPointLogDTO.setPointChange(500);
        memberPointLogDTO.setChangeType("신규 가입");
        MemberPointLogDTO memberPointLogDTO1 = memberPointLogService.save(memberPointLogDTO);
        log.info("Member Point Log Created: {}", memberPointLogDTO1);

        return ResponseEntity.ok("Member Saved");
    }

        // Read
            // Get One
    @GetMapping("/read/{memberId}")
    public ResponseEntity<MemberDTO> getMember(@PathVariable("memberId") String memberId) {
        MemberDTO memberDTO = memberService.findByMemberId(memberId);
        return ResponseEntity.ok(memberDTO);
    }

            // Get List
    @GetMapping("/list")
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        List<MemberDTO> memberDTOList = memberService.findAll();
        return ResponseEntity.ok(memberDTOList);
    }


        // Update
    @PostMapping("/update/{memberId}")
    public ResponseEntity<String> updateMember(@PathVariable("memberId") String memberId, @RequestBody MemberDTO memberDTO) {
        memberDTO.setMemberId(memberId);
        memberService.update(memberDTO);
        log.info("Member Updated : {}", memberDTO.toString());
        return ResponseEntity.ok("Member Updated");
    }


        // Delete
    @PostMapping("/delete/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable("memberId") String memberId) {
        // 1. 사업자 포인트 이력 전부 제거
        List<MemberPointLogDTO> memberPointLogDTOList = memberPointLogService.findByMemberId(memberId);
        for(MemberPointLogDTO memberPointLogDTO : memberPointLogDTOList) {
            memberPointLogService.delete(memberPointLogDTO.getLogId());
            log.info("Member {}'s Point Log Deleted", memberPointLogDTO.getMemberId());
        }
        log.info("Member {}'s Point Log All Deleted", memberId);

        // 2. 사업자 포인트 테이블 제거
        MemberPointDTO memberPointDTO = memberPointService.findByMemberId(memberId);
        memberPointService.delete(memberPointDTO.getPointId());
        log.info("Member {}'s Point Deleted", memberId);

        // 3. 회원 탈퇴
        memberService.delete(memberId);
        log.info("Member Deleted : {}", memberId);

        return ResponseEntity.ok("Member Deleted");
    }
    
    
    // 사업자 포인트 관련 메서드
        
        // Read
            // Get Myself
    @GetMapping("/point/find/{memberId}")
    public ResponseEntity<MemberPointDTO> findMyPoints(@PathVariable String memberId) {
        MemberPointDTO memberPointDTO = memberPointService.findByMemberId(memberId);
        return ResponseEntity.ok(memberPointDTO);
    }
            // Get List For Admin
    @GetMapping("/point/list")
    public ResponseEntity<List<MemberPointDTO>> getAllPoints() {
        List<MemberPointDTO> memberPointDTOList = memberPointService.findAll();
        return ResponseEntity.ok(memberPointDTOList);
    }
    
        // Update
            // ChargePoint
    @PostMapping("/point/memberCharge/{memberId}/{point}")
    public ResponseEntity<MemberPointDTO> memberChargePoint(@PathVariable String memberId, @PathVariable Integer point) {
        // 포인트 테이블에 충전
        MemberPointDTO memberPointDTO = memberPointService.findByMemberId(memberId);
        Integer totalPoint = memberPointDTO.getPointTotal();
        Integer balance = memberPointDTO.getPointBalance();

        memberPointDTO.setPointTotal(totalPoint + point);
        memberPointDTO.setPointBalance(balance + point);

        memberPointService.save(memberPointDTO);
        log.info("Member {}'s point charged : {}", memberId, point);

        // 포인트 이력에 기록

        MemberPointLogDTO memberPointLogDTO = new MemberPointLogDTO();
        memberPointLogDTO.setMemberId(memberId);
        memberPointLogDTO.setPointChange(point);
        memberPointLogDTO.setChangeType("포인트 충전");
        memberPointLogService.save(memberPointLogDTO);
        log.info("Member {}'s charge logged", memberId);

        return ResponseEntity.ok(memberPointDTO);
    }

            // Use Point (설문조사 후에 별도 로직으로 포인트 사용하도록 설정)

    // Delete (회원탈퇴에 포인트 테이블을 같이 삭제)


    
    // 사업자 포인트 이력 단독 메서드
        // Read
        // Get List Myself
    @GetMapping("/pointlog/list/{memberId}")
    public ResponseEntity<List<MemberPointLogDTO>> getMyPointLogs(@PathVariable String memberId) {
        List<MemberPointLogDTO> memberPointLogDTOList = memberPointLogService.findByMemberId(memberId);
        return ResponseEntity.ok(memberPointLogDTOList);
    }

        // Get All List For Admin
    @GetMapping("/pointlog/list/all")
    public ResponseEntity<List<MemberPointLogDTO>> getAllPointLogs() {
        List<MemberPointLogDTO> memberPointLogDTOList = memberPointLogService.findAll();
        return ResponseEntity.ok(memberPointLogDTOList);
    }


        // Delete (포인트 이력 개별 삭제, 회원탈퇴로 인한 삭제는 회원정보 Delete 에서)
    @PostMapping("/pointlog/delete/{logId}")
    public ResponseEntity<String> deletePointLog(@PathVariable long logId){
        memberPointLogService.delete(logId);
        log.info("Member {}'s point log deleted", logId);
        return ResponseEntity.ok("Log Deleted");
    }
}
