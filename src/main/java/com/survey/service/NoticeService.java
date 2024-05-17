package com.survey.service;

import com.survey.dto.NoticeDTO;
import com.survey.entity.Notice;
import com.survey.repository.AdminRepository;
import com.survey.repository.NoticeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private AdminRepository adminRepository;

    // Convert DTO to Entity
    public Notice convertDTO(NoticeDTO noticeDTO) {
        Notice notice = new Notice();
        if(noticeDTO.getId() != null) {
            notice.setId(noticeDTO.getId());
        }
        notice.setTitle(noticeDTO.getTitle());
        notice.setContent(noticeDTO.getContent());
        notice.setWriter(noticeDTO.getWriter());
        if(noticeDTO.getRegDate() != null && noticeDTO.getUpdateDate() != null){
            notice.setRegDate(noticeDTO.getRegDate());
            notice.setUpdateDate(noticeDTO.getUpdateDate());
        } else {
            notice.setRegDate(LocalDateTime.now());
            notice.setUpdateDate(LocalDateTime.now());
        }
        notice.setReadCount(
                noticeDTO.getReadCount() != null ? noticeDTO.getReadCount() : 0
        );

        return notice;
    }

    // Convert Entity to DTO
    public NoticeDTO convertEntity(Notice notice) {
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setId(notice.getId());
        noticeDTO.setTitle(notice.getTitle());
        noticeDTO.setWriter(notice.getWriter());
        noticeDTO.setContent(notice.getContent());
        noticeDTO.setRegDate(notice.getRegDate());
        noticeDTO.setUpdateDate(notice.getUpdateDate());
        noticeDTO.setReadCount(notice.getReadCount());

        return noticeDTO;
    }

    // Paging
    public Page<NoticeDTO> noticePage(int page){
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Notice> noticePage = noticeRepository.findAll(pageable);
        List<NoticeDTO> noticeDTOList = new ArrayList<>();
        for(Notice notice : noticePage.getContent()){
            NoticeDTO noticeDTO = convertEntity(notice);
            noticeDTOList.add(noticeDTO);
        }

        return new PageImpl<>(noticeDTOList, pageable, noticePage.getTotalElements());
    }

    // Paging
    public Page<NoticeDTO> noticePageforMainPage(){
        Pageable pageable = PageRequest.of(0, 3, Sort.by("id").descending());
        Page<Notice> noticePage = noticeRepository.findAll(pageable);
        List<NoticeDTO> noticeDTOList = new ArrayList<>();
        for(Notice notice : noticePage.getContent()){
            NoticeDTO noticeDTO = convertEntity(notice);
            noticeDTOList.add(noticeDTO);
        }

        return new PageImpl<>(noticeDTOList, pageable, noticePage.getTotalElements());
    }


    // Create
    @Transactional
    public NoticeDTO save(NoticeDTO noticeDTO) {
        Notice notice = convertDTO(noticeDTO);
        Notice saved = noticeRepository.save(notice);
        log.info("SAVED COMPLETE, ID: {}", saved.toString());

        return convertEntity(saved);
    }

    // Read
    // GET List
    public List<NoticeDTO> findAll() {
        List<Notice> notices = noticeRepository.findAllByOrderByIdDesc();
        List<NoticeDTO> noticeDTOList = new ArrayList<>();
        for (Notice notice : notices) {
            NoticeDTO noticeDTO = convertEntity(notice);
            noticeDTOList.add(noticeDTO);
        }
        return noticeDTOList;
    }

    // GET One
    public NoticeDTO findById(Long id) {
        Notice notice = noticeRepository.findById(id).orElse(null);
        NoticeDTO noticeDTO = convertEntity(notice);
        return noticeDTO;
    }

    // Update
    @Transactional
    public void update(NoticeDTO noticeDTO) {
        Notice notice = convertDTO(noticeDTO);
        Notice saved = noticeRepository.save(notice);
        log.info("UPDATED COMPLETE, ID: {}", saved.getId());
    }

    // Delete
    @Transactional
    public void delete(Long id) {
        Notice notice = noticeRepository.findById(id).orElse(null);
        noticeRepository.delete(notice);
        log.info("DELETED COMPLETE, ID: {}", id);
    }

    // +1 ReadCount
    @Transactional
    public NoticeDTO updateReadCount(NoticeDTO noticeDTO) {
        Notice notice = convertDTO(noticeDTO);
        notice.setReadCount(noticeDTO.getReadCount() + 1);
        Notice saved = noticeRepository.save(notice);
        log.info("Notice Id{}'s readCount is : {]", saved.getId(), saved.getReadCount());
        return convertEntity(saved);
    }
}
