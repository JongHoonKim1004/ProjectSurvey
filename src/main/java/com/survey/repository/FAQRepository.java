package com.survey.repository;

import com.survey.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
}
