package com.survey.repository;

import com.survey.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionsRepository extends JpaRepository<Options, String> {
}
