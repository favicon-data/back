package com.capstone.favicon.user.application;

import com.capstone.favicon.user.domain.DataRequest;
import com.capstone.favicon.user.domain.Question;
import com.capstone.favicon.user.domain.Answer;
import com.capstone.favicon.user.repository.DataRequestRepository;
import com.capstone.favicon.user.repository.QuestionRepository;
import com.capstone.favicon.user.repository.AnswerRepository;
import com.capstone.favicon.user.application.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestImpl implements RequestService {
    private final DataRequestRepository dataRequestRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    public List<DataRequest> getAllRequests() {
        return dataRequestRepository.findAll();
    }

    @Override
    @Transactional
    public DataRequest createRequest(DataRequest dataRequest) {
        return dataRequestRepository.save(dataRequest);
    }

    @Override
    @Transactional
    public DataRequest updateReviewStatus(Long requestId, DataRequest.ReviewStatus status) {
        DataRequest request = dataRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setReviewStatus(status);
        return dataRequestRepository.save(request);
    }

    @Override
    public List<Question> getQuestionsByUser(Long userId) {
        return questionRepository.findByUser_UserId(userId);
    }

    @Override
    public List<Answer> getAnswersByQuestion(Long questionId) {
        return answerRepository.findByQuestion_User_UserId(questionId);
    }
}