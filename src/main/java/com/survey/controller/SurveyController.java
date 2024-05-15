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
import java.util.List;

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
    @PostMapping("/response/create")
    @Transactional
    public ResponseEntity<?> createResponse(@RequestParam("surveyId") String surveyId, @RequestBody ResponseDTO responseDTO){
        // 기본 설정
            // 1. 설문 조사 및 응답에서 조사 DTO, 이름 추출
        SurveyDTO surveyDTO = surveyService.findBySurveyId(surveyId);
        String userId = responseDTO.getUsersId();
        String memberId = surveyDTO.getMemberId();
        OptionsDTO optionsDTO = optionsService.findByOptionsId(responseDTO.getOptionsId());
        Boolean terminate = optionsDTO.isTerminate();

            // 2. 이용자 관련 DTO 설정
        UsersPointDTO usersPointDTO = usersPointService.findByUsersId(userId);
        UsersPointLogDTO usersPointLogDTO = new UsersPointLogDTO();
        usersPointLogDTO.setUsersId(userId);

            // 3. 사업자 관련 DTO 설정
        MemberPointDTO memberPointDTO = memberPointService.findByMemberId(memberId);
        MemberPointLogDTO memberPointLogDTO = new MemberPointLogDTO();
        memberPointLogDTO.setMemberId(memberId);

        if(terminate){
            // 조기종료 확인 시 최소 포인트 지급
            Integer point = surveyDTO.getPointAtLeast();

            // 1. 이용자 최소 포인트 지급
            UsersPointDTO givedUsersPointDTO = usersPointService.givePoint(usersPointDTO, point);

            // 2. 이용자 최소 포인트 지급 이력 작성
            UsersPointLogDTO givedUsersPointLogDTO = usersPointLogService.savePlusLog(userId, point, "설문조사 응답 : 조사 조건 미달");

            // 3. 사업자 최소 포인트 사용 후 잔액 계산
            Integer memberPointBalance = memberPointDTO.getPointBalance() - point;

            // 3-1. 사업자 잔여 포인트 일정 금액 이하일 시, 경고 이메일 전송 (이메일 API 설계 후)
            String memberName = memberService.findByMemberId(memberId).getName();

            if(memberPointBalance < 10000){
                Integer code = emailService.createRandom();
                String content = emailService.createEmailWithCode("memberPoint", code);
                try{
                    emailService.sendEmail(memberName, content, "memberPoint");
                    log.info("Email Send : for member's point");
                } catch(Exception e){
                    e.printStackTrace();
                }
            }

            // 4. 사업자 최소 포인트 사용
            MemberPointDTO usedMemberPointDTO = memberPointService.usePoint(memberPointDTO, point);

            // 5. 사업자 최소 포인트 사용 이력 작성
            MemberPointLogDTO usedMemberPointLogDTO = memberPointLogService.saveMinusLog(memberId, point, "설문조사 참여 : " + surveyId);

            // 6. 설문조사 종료시 응시 인원 1 만큼 추가
            surveyService.plusSurveyParticipate(surveyDTO);

            return ResponseEntity.ok("terminated");

        } else {
            // 조기종료가 아닌 경우 다음 질문를 호출
            QuestionDTO questionDTO = questionService.findByQuestionId(responseDTO.getQuestionId());
            String questionId = responseDTO.getQuestionId();
            Integer questionNumber = questionDTO.getQuestionNumber();
            Integer nextQuestionNumber = questionNumber + 1;

            QuestionDTO nextQuestion = questionService.findByQuestionIdAndQuestionNumber(questionId, nextQuestionNumber);

            // 다음 문제가 있는 경우 NextQuestionDTO 를 설정하고 전송
            if(nextQuestion != null){
                // 2. NextQuestionDTO 설정
                NextQuestionDTO nextQuestionDTO = new NextQuestionDTO();
                nextQuestionDTO.setQuestion(nextQuestion);

                List<OptionsDTO> optionsDTOList = optionsService.findByQuestionId(questionId);
                nextQuestionDTO.setOptions(optionsDTOList);

                // 3. NextQuestionDTO 전송

                return ResponseEntity.ok(nextQuestionDTO);
            } else{
                // 다음 문제가 없는 경우 설문이 종료되었으므로 최대 포인트 지급
                Integer point = surveyDTO.getPoint();

                // 1. 이용자 최대 포인트 지급
                UsersPointDTO givedUsersPointDTO = usersPointService.givePoint(usersPointDTO, point);

                // 2. 이용자 최대 포인트 지급 이력 작성
                UsersPointLogDTO givedUsersPointLogDTO = usersPointLogService.savePlusLog(userId, point, "설문조사 응답");

                // 3. 사업자 최대 포인트 사용 후 잔액 계산
                Integer memberPointBalance = usersPointDTO.getPointBalance() - point;

                // 3-1. 사업자 잔여 포인트 일정 금액 이하일 시, 경고 이메일 전송 (이메일 API 설계 후)
                String memberName = memberService.findByMemberId(memberId).getName();

                if(memberPointBalance < 10000){
                    Integer code = emailService.createRandom();
                    String content = emailService.createEmailWithCode("memberPoint", code);
                    try{
                        emailService.sendEmail(memberName, content, "memberPoint");
                        log.info("Email Send : for member's point");
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }

                // 4. 사업자 최대 포인트 사용
                MemberPointDTO usedMemberPointDTO = memberPointService.usePoint(memberPointDTO, point);

                // 5. 사업자 최대 포인트 사용 이력 작성
                MemberPointLogDTO usedMemberPointLogDTO = memberPointLogService.saveMinusLog(memberId, point, "설문조사 참여 : " + surveyId);

                // 6. 설문조사 종료시 응시 인원 1 만큼 추가
                surveyService.plusSurveyParticipate(surveyDTO);

                return ResponseEntity.ok("finished");
            }
        }
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
    @PostMapping("/response/update/{responseId}")
    public ResponseEntity<String> updateResponse(@PathVariable String responseId, @RequestBody ResponseDTO responseDTO){
        responseDTO.setResponseId(responseId);
        responseService.save(responseDTO);
        log.info("Response Updated Id: {}", responseDTO.getResponseId());
        return ResponseEntity.ok("Response Updated");
    }

        // 응답은 개별적으로 지울 수 없음 (설문조사가 삭제되면서 목록을 없애는 것만 가능)
}
