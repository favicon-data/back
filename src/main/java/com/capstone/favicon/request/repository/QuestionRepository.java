package com.capstone.favicon.request.repository;

import com.capstone.favicon.request.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByUser_UserId(Long userId);
}