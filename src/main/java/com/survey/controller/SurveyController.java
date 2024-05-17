package com.survey.controller;

import com.survey.dto.*;
import com.survey.entity.Survey;
import com.survey.entity.UsersPoint;
import com.survey.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/survey/*")
@Slf4j
public class SurveyController {
    @Autowired
    private SurveyService surveyService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private OptionsService optionsService;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private UsersPointService usersPointService;
    @Autowired
    private UsersPointLogService usersPointLogService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberPointService memberPointService;
    @Autowired
    private MemberPointLogService memberPointLogService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UsersSurveyService usersSurveyService;
    @Autowired
    private MemberSurveyService memberSurveyService;
    @Autowired
    private UsersService usersService;


    // 설문조사 파트
        // Create Survey -> Get SurveyId
    @Transactional
    @PostMapping("/create")
    public ResponseEntity<SurveyDTO> createSurvey(@RequestBody SurveyDTO surveyDTO) {
        SurveyDTO savedSurvey = surveyService.save(surveyDTO);
        log.info("Survey created: {}", savedSurvey);

        String surveyId = savedSurvey.getSurveyId();
        MemberSurveyDTO memberSurveyDTO = new MemberSurveyDTO();
        memberSurveyDTO.setSurveyId(surveyId);
        memberSurveyDTO.setMemberId(savedSurvey.getMemberId());

        return ResponseEntity.ok(savedSurvey);
    }

        // Read
            // Get List
    @GetMapping("/list")
    public ResponseEntity<List<SurveyDTO>> listSurveys() {
        List<SurveyDTO> surveyDTOList = surveyService.findAll();
        return ResponseEntity.ok(surveyDTOList);
    }

            // Get List for Member
    @GetMapping("/list/member/{memberId}")
    public ResponseEntity<List<SurveyDTO>> listSurveysByMemberId(@PathVariable("memberId") String memberId) {
        List<SurveyDTO> surveyDTOList = surveyService.findByMemberId(memberId);
        return ResponseEntity.ok(surveyDTOList);
    }

            // Get One By SurveyId
    @GetMapping("/read/{surveyId}")
    public ResponseEntity<SurveyDTO> readSurvey(@PathVariable String surveyId) {
        SurveyDTO surveyDTO = surveyService.findBySurveyId(surveyId);
        return ResponseEntity.ok(surveyDTO);
    }

            // Get List can Participate
    @GetMapping("/list/active")
    public ResponseEntity<List<SurveyDTO>> listActiveSurveys() {
        List<SurveyDTO> surveyDTOList = surveyService.getActiveSurveys();
        log.info("Active surveys: {}", surveyDTOList);
        return ResponseEntity.ok(surveyDTOList);
    }


        // Update
    @PostMapping("/update/{surveyId}")
    public ResponseEntity<String> updateSurvey(@PathVariable String surveyId, @RequestBody SurveyDTO surveyDTO) {
        surveyDTO.setSurveyId(surveyId);
        surveyService.update(surveyDTO);
        log.info("Survey updated Id: {}", surveyDTO.getSurveyId());
        return ResponseEntity.ok("Survey updated");
    }

        // Delete
    @PostMapping("/delete/{surveyId}")
    public ResponseEntity<String> deleteSurvey(@PathVariable String surveyId) {
        // 1. 선택지 목록 삭제
        List<QuestionDTO> questionDTOList = questionService.findBySurveyId(surveyId);
        List<ResponseDTO> responseDTOList = new ArrayList<>();
        for(QuestionDTO questionDTO : questionDTOList) {
            List<ResponseDTO> responseDTOS = responseService.findByQuestionId(questionDTO.getQuestionId());
            for(ResponseDTO responseDTO : responseDTOS) {
                responseDTOList.add(responseDTO);
            }
        }

        for(ResponseDTO responseDTO : responseDTOList) {
            responseService.delete(responseDTO.getResponseId());
            log.info("Response deleted Id: {}", responseDTO.getResponseId());
        }
        log.info("Response All Deleted");

        // 2. 선택지 목록 삭제
        List<OptionsDTO> optionsDTOList = new ArrayList<>();
        for(QuestionDTO questionDTO : questionDTOList) {
            List<OptionsDTO> optionsDTOS = optionsService.findByQuestionId(questionDTO.getQuestionId());
            for(OptionsDTO optionsDTO : optionsDTOS) {
                optionsDTOList.add(optionsDTO);
            }
        }

        for(OptionsDTO optionsDTO : optionsDTOList) {
            optionsService.delete(optionsDTO.getOptionsId());
            log.info("Options deleted Id: {}", optionsDTO.getOptionsId());
        }
        log.info("Options All Deleted");

        // 3. 질문 목록 삭제
        for(QuestionDTO questionDTO : questionDTOList) {
            questionService.delete(questionDTO.getQuestionId());
            log.info("Question deleted Id: {}", questionDTO.getQuestionId());
        }
        log.info("Question All Deleted");

        // 4. 설문조사 삭제
        surveyService.delete(surveyId);
        log.info("Survey deleted Id: {}", surveyId);

        return ResponseEntity.ok("Survey deleted");
    }

    // 질문 & 선택지 파트
        // Create
            // 새 질문 & 선택지 생성
    @PostMapping("/question/create/{surveyId}")
    public ResponseEntity<NextQuestionDTO> createQuestion(@PathVariable String surveyId, @RequestBody NextQuestionDTO nextQuestionDTO) {
        QuestionDTO questionDTO = nextQuestionDTO.getQuestion();
        questionDTO.setSurveyId(surveyId);
        nextQuestionDTO.setQuestion(questionDTO);

        NextQuestionDTO nextQuestionDTO1 = optionsService.saveWithNextQuestionDTO(nextQuestionDTO);
        log.info("Question And Options created");

        return ResponseEntity.ok(nextQuestionDTO1);
    }

        // Read
            // NextQuestionDTO 호출
    @GetMapping("/question/call/{questionId}")
    public ResponseEntity<NextQuestionDTO> callQuestion(@PathVariable String questionId) {
        NextQuestionDTO nextQuestionDTO = optionsService.getNextQuestion(questionId);
        
        // 질문 호출 중에 응답이 이미 있다면 진행중인 설문의 응답을 수정하는 것 이므로 응답도 같이 호출한다
        List<ResponseDTO> responseDTOList = responseService.findByQuestionId(questionId);
        if(responseDTOList.size() > 0) {
            nextQuestionDTO.setResponse(responseDTOList);
        }
        return ResponseEntity.ok(nextQuestionDTO);
    }

            // NextQuestionDTO List 호출
    @GetMapping("/question/call/all/{surveyId}")
    public ResponseEntity<List<NextQuestionDTO>> callAllQuestionInSurvey(@PathVariable String surveyId){
        List<NextQuestionDTO> nextQuestionDTOList = optionsService.getNextQuestionList(surveyId);
        log.info("Get All Lists : {}", nextQuestionDTOList);

        return ResponseEntity.ok(nextQuestionDTOList);
    }

        // Update
            // 질문 변경
    @PostMapping("/question/update/question/{questionId}")
    public ResponseEntity<NextQuestionDTO> updateQuestion(@PathVariable String questionId, @RequestBody NextQuestionDTO nextQuestionDTO) {
        // 반환할 DTO 설정
        NextQuestionDTO nextQuestionDTO1 = new NextQuestionDTO();

        // 질문 DTO 변경
        QuestionDTO questionDTO = nextQuestionDTO.getQuestion();
        QuestionDTO questionDTO1 = questionService.update(questionDTO);
        nextQuestionDTO1.setQuestion(questionDTO1);
        log.info("Question updated Id: {}", questionDTO1.getQuestionId());

        // 선택지 DTO 목록 변경
        List<OptionsDTO> optionsDTOList = nextQuestionDTO.getOptions();
        List<OptionsDTO> optionsDTOList1 = new ArrayList<>();
        for(OptionsDTO optionsDTO : optionsDTOList) {
            optionsDTO.setQuestionId(questionId);
            OptionsDTO optionsDTO1 = optionsService.update(optionsDTO);
            optionsDTOList1.add(optionsDTO1);
            log.info("Options updated Id: {}", optionsDTO1.getOptionsId());
        }
        nextQuestionDTO1.setOptions(optionsDTOList1);

        return ResponseEntity.ok(nextQuestionDTO1);
    }


        // Delete
            // 질문 삭제
    @PostMapping("/question/delete/question/{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable String questionId){
        // 1. 선택지 목록 삭제
        List<OptionsDTO> optionsDTOList = optionsService.findByQuestionId(questionId);
        for(OptionsDTO optionsDTO : optionsDTOList){
            optionsService.delete(optionsDTO.getOptionsId());
            log.info("Options Deleted Id : {}", optionsDTO.getOptionsId());
        }
        log.info("Options All Deleted");

        // 2. 질문 삭제
        questionService.delete(questionId);
        log.info("Question Deleted Id : {}", questionId);

        return ResponseEntity.ok("Question Deleted");
    }

            // 선택지 삭제
    @PostMapping("/question/delete/options/{optionsId}")
    public ResponseEntity<String> deleteOptions(@PathVariable String optionsId){
        optionsService.delete(optionsId);
        log.info("Options Deleted Id : {}", optionsId);
        return ResponseEntity.ok("Options Deleted");
    }

    // 응답 파트
        // Create
    @PostMapping("/response/create/{surveyId}")
    @Transactional
    public ResponseEntity<Map<String, Object>> submitResponses(@PathVariable String surveyId, @RequestBody List<ResponseDTO> responseDTOs) {
        log.info("Response : {} ", responseDTOs.toString());
        boolean isTerminated = responseService.saveResponses(responseDTOs);
        Map<String, Object> result = new HashMap<>();
        result.put("isTerminated", isTerminated);
        return ResponseEntity.ok(result);
    }

        // Read
            // 설문조사 하나에 대한 응답을 모두 호출
    @GetMapping("/response/result/{surveyId}")
    public ResponseEntity<List<ResponseDTO>> getSurveyResult(@PathVariable String surveyId){
        List<QuestionDTO> questionDTOList = questionService.findBySurveyId(surveyId);
        List<ResponseDTO> responseDTOList = new ArrayList<>();
        for(QuestionDTO questionDTO : questionDTOList){
            List<ResponseDTO> dtoList = responseService.findByQuestionId(questionDTO.getQuestionId());
            responseDTOList.addAll(dtoList);
        }
        return ResponseEntity.ok(responseDTOList);
    }
        // Update


        // 응답은 개별적으로 지울 수 없음 (설문조사가 삭제되면서 목록을 없애는 것만 가능)


    // 설문조사 종료 후 포인트 지급 관련
        // 조기종료로 인한 최소 지급 포인트만 지급
    @Transactional
    @PostMapping("/end/{surveyId}/{usersId}/terminate")
    public ResponseEntity<String> terminateSurvey(@PathVariable String surveyId, @PathVariable String usersId) {
        // DTO 설정
        SurveyDTO surveyDTO = surveyService.findBySurveyId(surveyId);
        UsersPointDTO usersPointDTO = usersPointService.findByUsersId(usersId);
        UsersPointLogDTO usersPointLogDTO = new UsersPointLogDTO();
        UsersSurveyDTO usersSurveyDTO = new UsersSurveyDTO();

        String memberId = surveyDTO.getMemberId();
        MemberDTO memberDTO = memberService.findByMemberId(memberId);
        MemberPointDTO memberPointDTO = memberPointService.findByMemberId(memberId);
        MemberPointLogDTO memberPointLogDTO = new MemberPointLogDTO();
        MemberSurveyDTO memberSurveyDTO = new MemberSurveyDTO();

        log.info("All DTOs Set");

        // 포인트 설정
        Integer point = surveyDTO.getPointAtLeast();
        log.info("point : {}", point);

        // 이용자 포인트 적립
            // 1. 포인트 테이블 업데이트
        usersPointDTO.setPointTotal(usersPointDTO.getPointTotal() + point);
        usersPointDTO.setPointBalance(usersPointDTO.getPointBalance() + point);

        usersPointService.update(usersPointDTO);
        log.info("Users Points Updated : {}", usersPointDTO);

            // 2. 포인트 이력 생성
        usersPointLogDTO.setUsersId(usersId);
        usersPointLogDTO.setPointChange(point);
        usersPointLogDTO.setChangeType("설문 조사 : 조기 종료("+ surveyId + ")");

        UsersPointLogDTO usersPointLogDTO1 = usersPointLogService.save(usersPointLogDTO);
        log.info("Users Point Log Created: {}", usersPointLogDTO1);

            // 3. 이용자 설문 참여 이력 만들기
        usersSurveyDTO.setMemberId(memberId);
        usersSurveyDTO.setUsersId(usersId);
        usersSurveyDTO.setSurveyId(surveyId);
        usersSurveyDTO.setPointGiven(point);
        usersSurveyService.save(usersSurveyDTO);
        log.info("Users Survey Log Created : {}", usersSurveyDTO);

        // 사용자 포인트 사용
            // 1. 포인트 테이블 업데이트
        memberPointDTO.setPointUsed(memberPointDTO.getPointUsed() + point);
        memberPointDTO.setPointBalance(memberPointDTO.getPointBalance() - point);

        MemberPointDTO memberPointDTO1 = memberPointService.update(memberPointDTO);
        log.info("Member Points Updated : {}", memberPointDTO1);

            // 1-1. 포인트 잔액이 일정 수준 이하라면 이메일 전송
        if(memberPointDTO1.getPointBalance() < 10000){
            int code = emailService.createRandom();

            String content = emailService.createEmailWithCode("memberPoint", code);
            try{
                emailService.sendEmail(memberDTO.getName(), content, "memberPoint");
            } catch(Exception e){
                e.printStackTrace();
            }
        }

            // 2. 포인트 소모 이력 생성
        memberPointLogDTO.setMemberId(memberId);
        memberPointLogDTO.setPointChange(point * -1);
        memberPointLogDTO.setChangeType("설문조사 응시 : 조기종료(" + surveyId + ")");

        MemberPointLogDTO memberPointLogDTO1 = memberPointLogService.save(memberPointLogDTO);
        log.info("Member Points Log Created : {}", memberPointLogDTO1);

            // 3. 사업자 설문조사 응답자 이력 생성
        memberSurveyDTO.setMemberId(memberId);
        memberSurveyDTO.setSurveyId(surveyId);
        memberSurveyDTO.setPointGiven(point);
        memberSurveyDTO.setUsersId(usersId);

        MemberSurveyDTO memberSurveyDTO1 = memberSurveyService.save(memberSurveyDTO);
        log.info("Member Survey Log Created : {}", memberSurveyDTO1);

        log.info("All Process End");

        return ResponseEntity.ok("complete");
    }

    // 조기종료가 아닌 정상적으로 설문이 종료된 경우 최대 포인트 지급
    @Transactional
    @PostMapping("/end/{surveyId}/{usersId}")
    public ResponseEntity<String> endSurvey(@PathVariable String surveyId, @PathVariable String usersId) {
        // DTO 설정
        SurveyDTO surveyDTO = surveyService.findBySurveyId(surveyId);
        UsersPointDTO usersPointDTO = usersPointService.findByUsersId(usersId);
        UsersPointLogDTO usersPointLogDTO = new UsersPointLogDTO();
        UsersSurveyDTO usersSurveyDTO = new UsersSurveyDTO();

        String memberId = surveyDTO.getMemberId();
        MemberDTO memberDTO = memberService.findByMemberId(memberId);
        MemberPointDTO memberPointDTO = memberPointService.findByMemberId(memberId);
        MemberPointLogDTO memberPointLogDTO = new MemberPointLogDTO();
        MemberSurveyDTO memberSurveyDTO = new MemberSurveyDTO();

        log.info("All DTOs Set");

        // 포인트 설정
        Integer point = surveyDTO.getPoint();
        log.info("point : {}", point);

        // 이용자 포인트 적립
        // 1. 포인트 테이블 업데이트
        usersPointDTO.setPointTotal(usersPointDTO.getPointTotal() + point);
        usersPointDTO.setPointBalance(usersPointDTO.getPointBalance() + point);

        usersPointService.update(usersPointDTO);
        log.info("Users Points Updated : {}", usersPointDTO);

        // 2. 포인트 이력 생성
        usersPointLogDTO.setUsersId(usersId);
        usersPointLogDTO.setPointChange(point);
        usersPointLogDTO.setChangeType("설문 조사 : 종료("+ surveyId + ")");

        UsersPointLogDTO usersPointLogDTO1 = usersPointLogService.save(usersPointLogDTO);
        log.info("Users Point Log Created: {}", usersPointLogDTO1);

        // 3. 이용자 설문 참여 이력 만들기
        usersSurveyDTO.setMemberId(memberId);
        usersSurveyDTO.setUsersId(usersId);
        usersSurveyDTO.setSurveyId(surveyId);
        usersSurveyDTO.setPointGiven(point);
        usersSurveyService.save(usersSurveyDTO);
        log.info("Users Survey Log Created : {}", usersSurveyDTO);

        // 사용자 포인트 사용
        // 1. 포인트 테이블 업데이트
        memberPointDTO.setPointUsed(memberPointDTO.getPointUsed() + point);
        memberPointDTO.setPointBalance(memberPointDTO.getPointBalance() - point);

        MemberPointDTO memberPointDTO1 = memberPointService.update(memberPointDTO);
        log.info("Member Points Updated : {}", memberPointDTO1);

        // 1-1. 포인트 잔액이 일정 수준 이하라면 이메일 전송
        if(memberPointDTO1.getPointBalance() < 10000){
            int code = emailService.createRandom();

            String content = emailService.createEmailWithCode("memberPoint", code);
            try{
                emailService.sendEmail(memberDTO.getName(), content, "memberPoint");
            } catch(Exception e){
                e.printStackTrace();
            }
        }

        // 2. 포인트 소모 이력 생성
        memberPointLogDTO.setMemberId(memberId);
        memberPointLogDTO.setPointChange(point * -1);
        memberPointLogDTO.setChangeType("설문조사 응시 : 종료(" + surveyId + ")");

        MemberPointLogDTO memberPointLogDTO1 = memberPointLogService.save(memberPointLogDTO);
        log.info("Member Points Log Created : {}", memberPointLogDTO1);

        // 3. 사업자 설문조사 응답자 이력 생성
        memberSurveyDTO.setMemberId(memberId);
        memberSurveyDTO.setSurveyId(surveyId);
        memberSurveyDTO.setPointGiven(point);
        memberSurveyDTO.setUsersId(usersId);

        MemberSurveyDTO memberSurveyDTO1 = memberSurveyService.save(memberSurveyDTO);
        log.info("Member Survey Log Created : {}", memberSurveyDTO1);

        // 설문조사 참여인원 +1
        surveyService.plusSurveyParticipate(surveyDTO);

        log.info("All Process End");

        return ResponseEntity.ok("complete");
    }
}
