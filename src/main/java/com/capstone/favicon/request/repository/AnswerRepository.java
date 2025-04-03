package com.capstone.favicon.request.repository;

import com.capstone.favicon.request.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestion_User_UserId(Long questionId);
}