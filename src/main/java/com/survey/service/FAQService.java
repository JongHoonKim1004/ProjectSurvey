package com.survey.service;

import com.survey.dto.FAQDTO;
import com.survey.entity.FAQ;
import com.survey.repository.FAQRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FAQService {
    @Autowired
    private FAQRepository faqRepository;

    // Convert DTO to Entity
    public FAQ convertDTO(FAQDTO dto) {
        FAQ faq = new FAQ();
        if(dto.getFaqId() != null){
            faq.setFaqId(dto.getFaqId());
        }
        faq.setTitle(dto.getTitle());
        faq.setContent(dto.getContent());
        return faq;
    }

    // Convert Entity to DTO
    public FAQDTO convertFaq(FAQ faq) {
        FAQDTO dto = new FAQDTO();
        dto.setFaqId(faq.getFaqId());
        dto.setTitle(faq.getTitle());
        dto.setContent(faq.getContent());
        return dto;
    }

    // Create
    @Transactional
    public FAQDTO save(FAQDTO dto) {
        FAQ faq = convertDTO(dto);
        FAQ saved = faqRepository.save(faq);
        log.info("Saved: {}", saved.getTitle());

        return convertFaq(saved);
    }

    // Read
    // Get List
    public List<FAQDTO> findAll() {
        List<FAQ> faqs = faqRepository.findAll();
        List<FAQDTO> dtos = new ArrayList<>();
        for (FAQ faq : faqs) {
            dtos.add(convertFaq(faq));
        }
        return dtos;
    }

    // Get One
    public FAQDTO findById(Long id) {
        FAQ faq = faqRepository.findById(id).get();
        return convertFaq(faq);
    }


    // Update
    @Transactional
    public void update(FAQDTO dto) {
        FAQ faq = convertDTO(dto);
        FAQ saved = faqRepository.save(faq);
        log.info("UPDATED: {}", saved.getTitle());
    }

    // Delete
    @Transactional
    public void delete(Long id) {
        FAQ faq = faqRepository.findById(id).get();
        faqRepository.delete(faq);
        log.info("DELETED: {}", faq.getTitle());
    }

}
